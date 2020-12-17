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

package org.suren.autotest.web.framework.selenium.strategy;

import java.util.ArrayList;
import java.util.List;

import com.surenpi.autotest.webui.core.Locator;
import com.surenpi.autotest.webui.core.LocatorNotFoundException;
import com.surenpi.autotest.webui.ui.AbstractElement;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

import com.surenpi.autotest.utils.StringUtils;
import com.surenpi.autotest.webui.core.ElementSearchStrategy;
import com.surenpi.autotest.webui.ui.Element;
import org.suren.autotest.web.framework.selenium.locator.AbstractLocator;

/**
 * 根据查找元素的优先级（{@link PrioritySearchStrategy}）进行便历查找， 找不到返回null
 * 
 * @see PrioritySearchStrategy
 * @see ZoneSearchStrategy
 * @author linuxsuren
 */
@Component("cyleSearchStrategy")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CyleSearchStrategy implements ElementSearchStrategy<WebElement>
{
	private static final Logger logger = LoggerFactory.getLogger(CyleSearchStrategy.class);

	@Autowired
	private SeleniumEngine engine;
	private Element element;
	private WebElement targetWebElement;
	private WebElement parentElement;

	@Override
	public WebElement search(Element element)
	{
		this.element = element;
		List<By> byList = new ArrayList<By>();

		// TODO should order it
		for (Locator locator : element.getLocatorList()) {
			if (locator instanceof AbstractLocator) {
				byList.add(((AbstractLocator) locator).getBy());

				if (this.element instanceof AbstractElement) {
					((AbstractElement) this.element).setTimeOut(locator.getTimeout());
				}
			}
		}

		if (StringUtils.isNotBlank(element.getId()))
		{
			byList.add(By.id(element.getId()));
		}
		else if (StringUtils.isNotBlank(element.getCSS()))
		{
			byList.add(By.className(element.getCSS()));
		}
		else if (StringUtils.isNotBlank(element.getXPath()))
		{
			byList.add(By.xpath(element.getXPath()));
		}
		else if (StringUtils.isNotBlank(element.getLinkText()))
		{
			byList.add(By.linkText(element.getLinkText()));
		}
		else if (StringUtils.isNotBlank(element.getPartialLinkText()))
		{
			byList.add(By.partialLinkText(element.getPartialLinkText()));
		}
		else if (StringUtils.isNotBlank(element.getTagName()))
		{
			byList.add(By.tagName(element.getTagName()));
		}

		if (byList.size() == 0) {
			throw new LocatorNotFoundException("no any providers provided");
		}
		return cycleFindElement(byList);
	}

	private WebElement cycleFindElement(List<By> byList)
	{
		WebElement webEle = null;

		for (By by : byList)
		{
			try
			{
				webEle = findElement(by);
			}
			catch (WebDriverException e)
			{
				logger.info("cannot found element " + by);
			}

			if (webEle != null)
			{
				return webEle;
			}
		}

		return null;
	}

	/**
	 * 通用的元素查找方法
	 * @param by 具体的查找方法
	 * @return
	 */
	private WebElement findElement(By by)
	{
		WebDriver driver = engine.getDriver();
		driver = engine.turnToRootDriver(driver);

		WebElement ele;
		if(element.getTimeOut() > 0)
		{
			try {
				WebDriverWait wait = new WebDriverWait(driver, element.getTimeOut());
				wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			} catch (TimeoutException e) {
				// timeout might caused by it's in a invisible zone, so try to make it be visible
				ele = findElementFromDriver(driver, by);
				if (ele != null) {
					Actions actions = new Actions(driver);
					actions.moveToElement(ele).build().perform();
				}
				return ele;
			}
		}
		ele = findElementFromDriver(driver, by);
		return ele;
	}

	private WebElement findElementFromDriver(WebDriver driver, By by) {
		WebElement ele;
		if(parentElement != null)
		{
			ele = parentElement.findElement(by);
		}
		else
		{
			ele = driver.findElement(by);
		}
		return ele;
	}

	/**
	 * 查找多个元素
	 * @param by
	 * @return
	 */
	private List<WebElement> findElements(By by)
	{
		WebDriver driver = engine.getDriver();
		driver = engine.turnToRootDriver(driver);

		if(parentElement != null)
		{
			return parentElement.findElements(by);
		}
		else
		{
			return driver.findElements(by);
		}
	}

	@Override
	public String description()
	{
		return "循环元素定位搜索策略。";
	}

}
