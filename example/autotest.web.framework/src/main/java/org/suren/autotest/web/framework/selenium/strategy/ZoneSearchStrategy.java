package org.suren.autotest.web.framework.selenium.strategy;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
				driver = driver.switchTo().frame(value);
				
				logger.debug(String.format("iframe[%s] siwtch success.", value));
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
					driver = driver.switchTo().frame(index);
					
					logger.debug(String.format("iframe[%s] siwtch success.", value));
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
				List<WebElement> eles = driver.findElements(By.tagName("input"));
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

}
