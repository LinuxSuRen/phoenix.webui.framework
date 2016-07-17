package org.suren.autotest.web.framework.selenium.strategy;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
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

	public WebElement search(Element element)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
