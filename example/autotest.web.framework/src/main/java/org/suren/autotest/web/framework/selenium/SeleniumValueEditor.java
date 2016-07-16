/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ValueEditor;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.strategy.CyleSearchStrategy;
import org.suren.autotest.web.framework.selenium.strategy.PrioritySearchStrategy;
import org.suren.autotest.web.framework.selenium.strategy.ZoneSearchStrategy;

/**
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
public class SeleniumValueEditor implements ValueEditor
{
	
	@Autowired
	private PrioritySearchStrategy	prioritySearchStrategy;
	@Autowired
	private CyleSearchStrategy		cyleSearchStrategy;
	@Autowired
	private ZoneSearchStrategy		zoneSearchStrategy;

	public Object getValue(Element ele)
	{
		return prioritySearchStrategy.search(ele).getText();
	}

	public void setValue(Element ele, Object value)
	{
		if (value == null)
		{
			value = "";
		}

		prioritySearchStrategy.search(ele).sendKeys(value.toString());
	}

	public boolean isEnabled(Element element)
	{
		return prioritySearchStrategy.search(element).isEnabled();
	}

	public boolean isHidden(Element element)
	{
		return !prioritySearchStrategy.search(element).isDisplayed();
	}

}
