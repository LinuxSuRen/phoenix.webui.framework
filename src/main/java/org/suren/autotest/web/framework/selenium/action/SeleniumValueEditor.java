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

import com.surenpi.autotest.report.record.Action;
import com.surenpi.autotest.report.record.ActionType;
import com.surenpi.autotest.report.record.NormalRecord;
import com.surenpi.autotest.webui.ui.AbstractElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.annotation.AutoModule;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

import com.surenpi.autotest.utils.StringUtils;
import com.surenpi.autotest.webui.action.AdvanceValueEditor;
import com.surenpi.autotest.webui.action.ValueEditor;
import com.surenpi.autotest.webui.ui.Element;
import org.suren.autotest.web.framework.settings.Cache;

/**
 * 给文本框中填入值
 * @author linuxsuren
 */
@Component
public class SeleniumValueEditor implements ValueEditor, AdvanceValueEditor
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumValueEditor.class);

	@Autowired
	private SeleniumEngine			engine;
	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;

	@Override
	public Object getValue(Element ele)
	{
		return searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele).getText();
	}

	/**
	 * 实现逻辑：点击文本框、清空文本框、填入值 
	  */
	@Override
	public void setValue(Element ele, Object value)
	{
		fillValue(ele, value, false);
	}

	@Override
	public void appendValue(Element ele, Object value)
	{
		fillValue(ele, value, true);
	}

	@Override
	public void fillNotBlankValue(Element ele, Object value)
	{
		if(value == null || StringUtils.isBlank(value.toString()))
		{
			throw new RuntimeException("Can not allow null or empty value!");
		}
		
		fillValue(ele, value, true);
	}
	
	/**
	 * 填入值，如果目标元素有readonly，则不做任何操作
	 * @param ele 目标元素
	 * @param value 要填入的值，null会当作空字符串
	 * @param append 是否追加
	 */
	private void fillValue(Element ele, Object value, boolean append)
	{
		if(value == null)
		{
			value = "";
		}

		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele);
		if(webEle != null)
		{
			String readonlyAttr = webEle.getAttribute("readonly");
			if(StringUtils.isNotBlank(readonlyAttr))
			{
				logger.warn("{} is readonly, will do not call method setValue.", webEle.toString());
				return;
			}
			
			String valueStr = value.toString();
			try
			{
				fill(webEle, valueStr, append);
			}
			catch(WebDriverException e)
			{
				if(e.getMessage().contains("is not clickable at point"))
				{
					((JavascriptExecutor) engine.getDriver()).executeScript("arguments[0].scrollIntoView();", webEle);

					fill(webEle, valueStr, append);
				}
				else
				{
					e.printStackTrace();
				}
			}

			Object extraData = null;
			if (ele instanceof AbstractElement) {
				extraData = ((AbstractElement) ele).getData("data");
			}
			if (extraData != null) {
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
					action.setType(ActionType.INPUT);
					action.setDescription(String.format("[%s] value filled", extraData));
					action.setValue(valueStr);
					normalRecord.getActions().add(action);
				}
				logger.info(String.format("[%s] value filled, value: '%s'", extraData, value) + moduel);
			} else {
				logger.info(String.format("[%s] value filled", ele));
			}
		}
		else
		{
			logger.error(String.format("can not found element [%s].", ele));
		}
	}

	/**
	 * 填入值
	 * @param webEle
	 * @param value
	 * @param append
	 */
	private void fill(WebElement webEle, String value, boolean append)
	{
		webEle.click();
		if(!append)
		{
			webEle.clear();
		}
		webEle.sendKeys(value);
	}

	@Override
	public void submit(Element ele)
	{
		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele);

		try {
			webEle.submit();
		} catch (NoSuchElementException e) {
			logger.info("perhaps this element is not in a form", e);

			WebDriver driver = engine.getDriver();
			new Actions(driver).sendKeys(webEle, Keys.ENTER).perform();
		}
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

}
