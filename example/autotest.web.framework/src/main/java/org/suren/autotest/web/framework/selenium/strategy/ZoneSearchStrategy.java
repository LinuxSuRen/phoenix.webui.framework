package org.suren.autotest.web.framework.selenium.strategy;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.Locator;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * 区域定位元素查找策略
 * 
 * @see PrioritySearchStrategy
 * @see CyleSearchStrategy
 * @author suren
 * @date Jul 16, 2016 7:27:09 PM
 */
@Component("zoneSearchStrategy")
public class ZoneSearchStrategy implements ElementSearchStrategy<WebElement>
{
	private static final Logger logger = LoggerFactory.getLogger(ZoneSearchStrategy.class);
	
	@Autowired
	private SeleniumEngine engine;

	@Override
	public WebElement search(Element element)
	{
		WebElement webEle = null;
		List<Locator> locators = element.getLocatorList();
		if(locators == null)
		{
			return webEle;
		}
		
		logger.info(String.format("zone search strategy, locators count[%s].", locators.size()));
		
		WebDriver driver = engine.getDriver();
		for(Locator locator : locators)
		{
			By by = null;
			String type = locator.getType();
			String value = locator.getValue();
			if("byId".equals(type))
			{
				by = By.id(value);
			}
			else if("byIFrame".equals(type))
			{
				driver = engine.turnToRootDriver(driver);
				
				iframeWait(driver, locator.getTimeout(), value);
				
//				driver = driver.switchTo().frame(value);
				
				logger.debug(String.format("iframe[%s] siwtch success by nameOrId.", value));
			}
			else if("byIFrameIndex".equals(type))
			{
				int index = -1;
				
				try
				{
					index = Integer.parseInt(value);
				} catch(NumberFormatException e) {}
				
				if(index != -1)
				{
					driver = engine.turnToRootDriver(driver);
					
					logger.debug(driver.getCurrentUrl());
					iframeWait(driver, locator.getTimeout(), index);

//					driver = driver.switchTo().frame(index);
					
					logger.debug(String.format("iframe[%s] siwtch success by index.", value));
				}
				else
				{
					logger.error(String.format("invalid iframe index [%s].", value));
				}
			}
			else if("byLinkText".equals(type))
			{
				by = By.linkText(value);
			}
			else if("byText".equals(type))
			{
				driver.findElements(By.tagName(""));
			}
			else if("byValue".equals(type))
			{
				By tmpBy = By.tagName("input");
				elementWait(driver, locator.getTimeout(), tmpBy);
				List<WebElement> eles = driver.findElements(tmpBy);
				for(WebElement item : eles)
				{
					if(item.getAttribute("value").equals(locator.getValue()))
					{
						return item;
					}
				}
			}
			else if("byXpath".equals(type))
			{
				by = By.xpath(value);
			}
			
			if(by == null)
			{
				continue;
			}
			
			try
			{
				elementWait(driver, locator.getTimeout(), by);
				
				if(webEle == null)
				{
					webEle = driver.findElement(by);
				}
				else
				{
					webEle = webEle.findElement(by);
				}
			}
			catch(NoSuchElementException e)
			{
				logger.error(
						String.format("can not found element by[%s]", by), e);
				
				break;
			}
		}
		
		return webEle;
	}

	/**
	 * 根据超时时间来等待元素
	 * @param locator
	 * @param by
	 */
	@SuppressWarnings("unchecked")
	private void elementWait(WebDriver driver, long timeout, By by)
	{
		eleWait(driver, timeout, ExpectedConditions.visibilityOfElementLocated(by));
	}

	@SuppressWarnings("unchecked")
	private void iframeWait(WebDriver driver, long timeout, int index)
	{
		eleWait(driver, timeout, ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
	}
	
	@SuppressWarnings("unchecked")
	private void iframeWait(WebDriver driver, long timeout, String locator)
	{
		eleWait(driver, timeout, ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	
	@SuppressWarnings("unchecked")
	private void eleWait(WebDriver driver, long timeout, ExpectedCondition<? extends SearchContext> ...isTrueArray)
	{
		if(timeout > 0 && isTrueArray != null && isTrueArray.length > 0)
		{
			logger.debug(String.format("prepare to waiting [%s] seconds.", timeout));
			
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			for(ExpectedCondition<? extends SearchContext> isTrue : isTrueArray)
			{
				wait.until(isTrue);
			}
			
			logger.debug(String.format("prepare to waiting [%s] seconds done.", timeout));
		}
	}

}
