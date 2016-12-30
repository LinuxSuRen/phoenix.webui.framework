package org.suren.autotest.web.framework.selenium.strategy;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.ElementsSearchStrategy;
import org.suren.autotest.web.framework.core.LocatorNotFoundException;
import org.suren.autotest.web.framework.core.ui.AbstractElement;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * 查找元素策略，找不到对应的元素会抛出异常</br>
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
 * @author suren
 * @date Jul 16, 2016 6:45:44 PM
 */
@Component("prioritySearchStrategy")
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
			return findElement(by);
		}
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
		else if (StringUtils.isNoneBlank(element.getCSS()))
		{
			String css = element.getCSS();
			if(element instanceof AbstractElement)
			{
				css = ((AbstractElement) element).paramTranslate(css);
			}
			
			String[] cssArray = css.split(" ");
			by = By.className(cssArray[0]);
			List<WebElement> elementList = findElements(by);
			for(WebElement ele : elementList)
			{
				new Actions(engine.getDriver()).moveToElement(ele);
				if(css.equals(ele.getAttribute("class")))
				{
					targetWebElement = ele;
					break;
				}
			}
			
			return null;
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
		else if (StringUtils.isNoneBlank(element.getPartialLinkText()))
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
		else
		{
			throw new LocatorNotFoundException();
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
		
		if(element.getTimeOut() > 0)
		{
			WebDriverWait wait = new WebDriverWait(driver, element.getTimeOut());
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		}
		
		if(parentElement != null)
		{
			return parentElement.findElement(by);
		}
		else
		{
			return driver.findElement(by);
		}
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
}