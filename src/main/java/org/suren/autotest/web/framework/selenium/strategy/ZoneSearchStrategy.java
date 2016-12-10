package org.suren.autotest.web.framework.selenium.strategy;

import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.Locator;
import org.suren.autotest.web.framework.core.ui.AbstractElement;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.locator.AbstractLocator;

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
	
	private int failedCount = 0;
	private int maxFailed = 6;

	@SuppressWarnings("unchecked")
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
		
		AbstractElement absEle = null;
		if(element instanceof AbstractElement)
		{
			absEle = (AbstractElement) element;
		}
		
		WebDriver driver = engine.getDriver();
		for(Locator locator : locators)
		{
			if(!(locator instanceof AbstractLocator<?>))
			{
				logger.warn(String.format("Unknow locator type, not subclass of AbstractLocator, [%s].",
						locator.getClass()));
				continue;
			}
			
			AbstractLocator<WebElement> absLocator = ((AbstractLocator<WebElement>) locator);
			if(absEle != null)
			{
				//动态参数转换
				absLocator.setValue(absEle.paramTranslate(absLocator.getValue()));
			}

			if(webEle != null)
			{
				webEle = retry(absLocator, webEle); 
			}
			else
			{
				webEle = retry(absLocator, driver);
			}
		}
		
		return webEle;
	}
	
	/**
	 * 失败重试
	 * @param absLocator
	 * @param webEle
	 * @return
	 */
	private WebElement retry(AbstractLocator<WebElement> absLocator, SearchContext webEle)
	{
		WebElement result = null;
		
		if(webEle != null)
		{
			result = absLocator.findElement(webEle);
		}
		else
		{
			result = absLocator.findElement(webEle);
		}
		
		if(result != null || ++failedCount > maxFailed)
		{
			return result;
		}
		else
		{
			return retry(absLocator, webEle);
		}
	}
}
