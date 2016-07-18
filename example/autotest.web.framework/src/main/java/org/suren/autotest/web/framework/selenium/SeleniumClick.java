/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ClickAble;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

/**
 * 通过Selenium实现点击（单击、双击）
 * 
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
public class SeleniumClick implements ClickAble
{

	@Autowired
	private SeleniumEngine			engine;
	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;

	@Override
	public void click(Element ele)
	{
		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele);
		try
		{
			webEle.click();
		}
		catch(WebDriverException e)
		{
			if(e.getMessage().contains("Element is not clickable at point"))
			{
				new Actions(engine.getDriver()).click(webEle);
			}
		}
	}

	@Override
	public void dbClick(Element ele)
	{
		Actions actions = new Actions(engine.getDriver());
		actions.doubleClick(searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele));
	}

	@Override
	public boolean isEnabled(Element element)
	{
		return searchStrategyUtils.findStrategy(WebElement.class, element).search(element).isEnabled();
	}

	@Override
	public boolean isHidden(Element element)
	{
		return !searchStrategyUtils.findStrategy(WebElement.class, element).search(element).isDisplayed();
	}

}
