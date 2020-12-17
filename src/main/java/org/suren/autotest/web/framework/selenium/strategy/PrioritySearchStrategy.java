/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.selenium.strategy;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.locator.AbstractLocator;
import org.suren.autotest.web.framework.selenium.locator.SeleniumTextLocator;
import org.suren.autotest.web.framework.selenium.locator.SeleniumXAttrLocator;

import com.surenpi.autotest.utils.StringUtils;
import com.surenpi.autotest.webui.core.AutoTestException;
import com.surenpi.autotest.webui.core.ElementSearchStrategy;
import com.surenpi.autotest.webui.core.ElementsSearchStrategy;
import com.surenpi.autotest.webui.core.Locator;
import com.surenpi.autotest.webui.core.LocatorNotFoundException;
import com.surenpi.autotest.webui.ui.AbstractElement;
import com.surenpi.autotest.webui.ui.Element;
import org.suren.autotest.web.framework.selenium.locator.SeleniumXPathLocator;

/**
 * 查找元素策略，找不到对应的元素会抛出异常
 * <ul>
 * <li>通过id查找</li>
 * <li>通过css样式查找</li>
 * <li>通过xpath查找</li>
 * <li>通过超链接文本查找</li>
 * <li>通过超链接部分文本查找</li>
 * <li>根据标签名称来查找</li>
 * </ul>
 * 
 * @see CyleSearchStrategy
 * @see ZoneSearchStrategy
 * @author linuxsuren
 */
@Component("prioritySearchStrategy")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrioritySearchStrategy implements ElementSearchStrategy<WebElement>,
		ElementsSearchStrategy<WebElement>, ParentElement
{

	@Autowired
	private SeleniumEngine engine;
	private Element element;
	private WebElement targetWebElement;
	private WebElement parentElement;

	/**
	 * 根据定位元素的优先级来进行元素查找
	 */
	@Override
	public WebElement search(Element element)
	{
		By by = buildBy(element);
		if(targetWebElement != null)
		{
			return targetWebElement;
		}
		else
		{
			try {
				return findElement(by);
			} catch (TimeoutException e) {
				if (element instanceof AbstractElement) {
					String eleName = ((AbstractElement) element).getDataStr("data");
					if (eleName != null) {
						System.out.println("timeout when finding element: " + eleName);
					}
				}
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<WebElement> searchAll(Element element)
	{
		By by = buildBy(element);
		return findElements(by);
	}
	
	/**
	 * 构建定位方法
	 * @param element
	 * @return
	 */
	private By buildBy(Element element)
	{
		By by = null;

		this.element = element;
		
		if (StringUtils.isNotBlank(element.getId()))
		{
			String orginId = element.getId();
			String paramId = orginId;
			if(element instanceof AbstractElement)
			{
				paramId = ((AbstractElement) element).paramTranslate(paramId);
			}
			by = By.id(paramId);
		}
		else if (StringUtils.isNotBlank(element.getCSS()))
		{
			String css = element.getCSS();
			if(element instanceof AbstractElement)
			{
				css = ((AbstractElement) element).paramTranslate(css);
			}
			
			String[] cssArray = css.split(" ");
			by = By.className(cssArray[0]);

			if(cssArray.length > 1) {
				List<WebElement> elementList = findElements(by);
				for (WebElement ele : elementList) {
					new Actions(engine.getDriver()).moveToElement(ele);
					if (css.equals(ele.getAttribute("class"))) {
						targetWebElement = ele;
						return null;
					}
				}

				throw new AutoTestException("Can not found parent element by css: " + css);
			}
		}
		else if(StringUtils.isNotBlank(element.getName()))
		{
			String orginName = element.getName();
			String paramName = orginName;
			if(element instanceof AbstractElement)
			{
				paramName = ((AbstractElement) element).paramTranslate(paramName);
			}
			by = By.name(paramName);
		}
		else if (StringUtils.isNotBlank(element.getXPath()))
		{
			String orginXPath = element.getXPath();
			String paramXPath = orginXPath;
			if(element instanceof AbstractElement)
			{
				paramXPath = ((AbstractElement) element).paramTranslate(paramXPath);
			}
			by = By.xpath(paramXPath);
		}
		else if (StringUtils.isNotBlank(element.getLinkText()))
		{
			String orginLinkText = element.getLinkText();
			String paramLinkText = orginLinkText;
			if(element instanceof AbstractElement)
			{
				paramLinkText = ((AbstractElement) element).paramTranslate(paramLinkText);
			}
			by = By.linkText(paramLinkText);
		}
		else if (StringUtils.isNotBlank(element.getPartialLinkText()))
		{
			String orginPartialLinkText = element.getPartialLinkText();
			String paramPartialLinkText = orginPartialLinkText;
			if(element instanceof AbstractElement)
			{
				paramPartialLinkText = ((AbstractElement) element).paramTranslate(paramPartialLinkText);
			}
			by = By.partialLinkText(paramPartialLinkText);
		}
		else if (StringUtils.isNotBlank(element.getTagName()))
		{
			String orginTagName = element.getTagName();
			String paramTagName = orginTagName;
			if(element instanceof AbstractElement)
			{
				paramTagName = ((AbstractElement) element).paramTranslate(paramTagName);
			}
			by = By.tagName(paramTagName);
		}
		
        for(Locator locator : element.getLocatorList())
        {
            Class<?> locatorClz = locator.getClass();
            if(locatorClz.equals(SeleniumXAttrLocator.class)
                    || locatorClz.equals(SeleniumTextLocator.class)
					|| locatorClz.equals(SeleniumXPathLocator.class))
            {
                by = ((AbstractLocator<?>) locator).getBy();
                
                break;
            }
        }
        
        if(by == null)
        {
            throw new LocatorNotFoundException("PrioritySearchStrategy");
        }
		
		return by;
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
	public void setParent(WebElement parentWebElement)
	{
		this.parentElement = parentWebElement;
	}

	@Override
	public String description()
	{
		return "根据预先给定的元素定位方法优先级进行搜索，直到找到元素或者定位方法穷举完毕。"
				+ "如果元素指定了显式的查找超时时间，则采用可见性的超时等待。";
	}
}