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
import org.suren.autotest.web.framework.core.LocatorNotFoundException;
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
public class PrioritySearchStrategy implements ElementSearchStrategy<WebElement>
{

	@Autowired
	private SeleniumEngine engine;

	@Override
	public WebElement search(Element element)
	{
		By by = null;

		if (StringUtils.isNotBlank(element.getId()))
		{
			by = By.id(element.getId());
		}
		else if (StringUtils.isNoneBlank(element.getCSS()))
		{
			String css = element.getCSS();
			String[] cssArray = css.split(" ");
			by = By.className(cssArray[0]);
			List<WebElement> elementList = findElements(by);
			for(WebElement ele : elementList)
			{
				new Actions(engine.getDriver()).moveToElement(ele);
				if(css.equals(ele.getAttribute("class")))
				{
					return ele;
				}
			}
			
			return null;
		}
		else if(StringUtils.isNotBlank(element.getName()))
		{
			by = By.name(element.getName());
		}
		else if (StringUtils.isNotBlank(element.getXPath()))
		{
			by = By.xpath(element.getXPath());
		}
		else if (StringUtils.isNotBlank(element.getLinkText()))
		{
			by = By.linkText(element.getLinkText());
		}
		else if (StringUtils.isNoneBlank(element.getPartialLinkText()))
		{
			by = By.partialLinkText(element.getPartialLinkText());
		}
		else if (StringUtils.isNotBlank(element.getTagName()))
		{
			by = By.tagName(element.getTagName());
		}
		else
		{
			throw new LocatorNotFoundException();
		}

		return findElement(by);
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
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		return driver.findElement(by);
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
		
		return driver.findElements(by);
	}
}