package org.suren.autotest.web.framework.selenium.strategy;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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
		
		for(Locator locator : locators)
		{
			By by = null;
			String type = locator.getType();
			if("byId".equals(type))
			{
				by = By.id(locator.getValue());
			}
			
			if(by == null)
			{
				continue;
			}
			
			if(webEle == null)
			{
				webEle = engine.getDriver().findElement(by);
			}
			else
			{
				webEle = webEle.findElement(by);
			}
		}
		
		return webEle;
	}

}
