/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ClickAble;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.strategy.CyleSearchStrategy;
import org.suren.autotest.web.framework.selenium.strategy.PrioritySearchStrategy;
import org.suren.autotest.web.framework.selenium.strategy.ZoneSearchStrategy;

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
	private PrioritySearchStrategy	prioritySearchStrategy;
	@Autowired
	private CyleSearchStrategy		cyleSearchStrategy;
	@Autowired
	private ZoneSearchStrategy		zoneSearchStrategy;

	public void click(Element ele)
	{
		prioritySearchStrategy.search(ele).click();
	}

	public void dbClick(Element ele)
	{
		Actions actions = new Actions(engine.getDriver());
		actions.doubleClick(prioritySearchStrategy.search(ele));
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
