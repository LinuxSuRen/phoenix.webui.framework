/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.selenium.action;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Iterator;
import java.util.Set;

import com.surenpi.autotest.report.record.Action;
import com.surenpi.autotest.report.record.ActionType;
import com.surenpi.autotest.report.record.NormalRecord;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.annotation.AutoModule;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

import com.surenpi.autotest.webui.action.ClickAble;
import com.surenpi.autotest.webui.core.ElementSearchStrategy;
import com.surenpi.autotest.webui.ui.AbstractElement;
import com.surenpi.autotest.webui.ui.Element;
import com.surenpi.autotest.webui.ui.FileUpload;
import org.suren.autotest.web.framework.settings.Cache;

/**
 * 通过Selenium实现点击（单击、双击）
 * @author linuxsuren
 */
@Component
public class SeleniumClick implements ClickAble
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumClick.class);
	
	/** 失败后重试的最大次数 */
	private int maxRetry = 3;
	
	private static final String ERR_TIMES = "CLICK_ERR_TIMES";
	
	@Autowired
	private SeleniumEngine			engine;
	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;

	@Override
	public void click(Element ele)
	{
		int errorTimes = 0;
		
		if(ele instanceof AbstractElement)
		{
			Object errObj = ((AbstractElement) ele).getData(ERR_TIMES);
			if(errObj instanceof Integer)
			{
				errorTimes = (Integer) errObj;
				if(isReachMaxErr(errorTimes))
				{
					return;
				}
			}
		}
		
		if(errorTimes > 0)
		{
			logger.warn(String.format("Click operation retry times [%s].", errorTimes));
		}
		
		ElementSearchStrategy<WebElement> searchStrategy =
				searchStrategyUtils.findStrategy(WebElement.class, ele);
		WebElement webEle = searchStrategy.search(ele);
		if(webEle == null)
		{
			throw new RuntimeException(String.format("Element [%s] can not found "
					+ "by strategy [%s]!", ele, searchStrategy));
		}
		
		try
		{
			//对于远程服务的文件上传，不移动鼠标
			if(!(ele instanceof FileUpload) && !(engine.getDriver() instanceof RemoteWebDriver))
			{
				Dimension size = webEle.getSize();
				Point loc = webEle.getLocation();
				int toolbarHeight = engine.getToolbarHeight();
				int x = size.getWidth() / 2 + loc.getX();
				int y = size.getHeight() / 2 + loc.getY() + toolbarHeight;
				
				new Robot().mouseMove(x, y);
			}
			
			webEle.click();
			
			//如果是a标签锚点的话，根据target属性来决定是否要切换window句柄
			String tagName = webEle.getTagName();
			String targetAttr = webEle.getAttribute("target");
			if("a".equals(tagName) && "_blank".equals(targetAttr))
			{
				WebDriver driver = engine.getDriver();
				Set<String> handlers = driver.getWindowHandles();
				Iterator<String> it = handlers.iterator();
				while(it.hasNext())
				{
					String name = it.next();
					
					driver.switchTo().window(name);
				}
			}
		}
		catch(WebDriverException e)
		{
			if(ele instanceof AbstractElement)
			{
				((AbstractElement) ele).putData(ERR_TIMES, ++errorTimes);
			}

			String errLog = String.format("元素[%s]点击操作发生错误。", webEle);
			if(isReachMaxErr(errorTimes))
			{
				logger.error(errLog, e);
			}
			else
			{
				logger.trace(errLog, e);
			}

			//如果由于目标元素不在可见区域导致的异常，尝试滚动屏幕
			if(e.getMessage().contains("is not clickable at point"))
			{
				logger.info("Will retry click operation, after element move.");
				
				new Actions(engine.getDriver()).moveToElement(webEle, -50, -50).perform();
				
				WebDriverWait wait = new WebDriverWait(engine.getDriver(), 30);
				((JavascriptExecutor) engine.getDriver()).executeScript("arguments[0].scrollIntoView();", webEle, -50, -50);
				wait.until(ExpectedConditions.visibilityOf(webEle));
				
				click(ele);
			}
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}

		Object extraData = null;
		if (ele instanceof AbstractElement) {
			extraData = ((AbstractElement) ele).getData("data");
		}
		if (extraData != null) {
			logger.info(extraData + " click done");
			String moduel = "";
			for (StackTraceElement item : Thread.currentThread().getStackTrace()) {
				AutoModule automodule = null;
				try {
					automodule = Class.forName(item.getClassName()).getAnnotation(AutoModule.class);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if (automodule != null) {
					moduel = automodule.name();
					break;
				}
			}

			if (Cache.getInstance().get(moduel) != null) {
				NormalRecord normalRecord = (NormalRecord) Cache.getInstance().get(moduel);
				Action action = new Action();
				action.setType(ActionType.CLICK);
				action.setDescription(extraData + " click done");
				normalRecord.getActions().add(action);
			}
		} else {
			logger.info(ele + " click done");
		}
	}

	/**
	 * @param errorTimes 错误次数
	 * @return 达到最大错误重试次数返回true，否则返回false
     */
	private boolean isReachMaxErr(int errorTimes)
	{
		return errorTimes >= maxRetry;
	}

	@Override
	public void dbClick(Element ele)
	{
		Actions actions = new Actions(engine.getDriver());
		actions.doubleClick(searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele));
	}

	@Override
	public boolean isEnabled(Element element)
	{
		return searchStrategyUtils.findStrategy(WebElement.class, element).search(element).isEnabled();
	}

	@Override
	public boolean isHidden(Element element)
	{
		return !searchStrategyUtils.findStrategy(WebElement.class, element).search(element).isDisplayed();
	}

	/**
	 * @return 失败后重试的最大次数
	 */
	public int getMaxRetry()
	{
		return maxRetry;
	}

	/**
	 * @param maxRetry 失败后重试的最大次数（默认为3）
	 */
	public void setMaxRetry(int maxRetry)
	{
		this.maxRetry = maxRetry;
	}

}
