/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ValueEditor;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

/**
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
public class SeleniumValueEditor implements ValueEditor
{

	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;

	public Object getValue(Element ele)
	{
		return searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele).getText();
	}

	public void setValue(Element ele, Object value)
	{
		if (value == null)
		{
			value = "";
		}

		searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele).sendKeys(value.toString());
	}

	public boolean isEnabled(Element element)
	{
		return searchStrategyUtils.findStrategy(WebElement.class, element).search(element).isEnabled();
	}

	public boolean isHidden(Element element)
	{
		return !searchStrategyUtils.findStrategy(WebElement.class, element).search(element).isDisplayed();
	}

}
