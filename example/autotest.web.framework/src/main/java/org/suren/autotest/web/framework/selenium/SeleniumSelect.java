/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.SelectAble;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

/**
 * @author suren
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT 2016年7月1日
 */
@Component
public class SeleniumSelect implements SelectAble
{

	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;

	public boolean selectByText(Element element, String text)
	{
		Select select = createSelect(element);
		if(select != null)
		{
			select.selectByVisibleText(text);
			return true;
		}

		return false;
	}

	public boolean selectByValue(Element element, String value)
	{
		Select select = createSelect(element);
		if(select != null)
		{
			select.selectByValue(value);
			return true;
		}

		return false;
	}

	public boolean selectByIndex(Element element, int index)
	{
		Select select = createSelect(element);
		if(select != null)
		{
			select.selectByIndex(index);
			return true;
		}

		return false;
	}

	public boolean selectAll(Element element)
	{
		if(!isMultiple(element)) {
			return false;
		}
		
		List<Element> optionList = getOptions(element);
		if(optionList == null) {
			return false;
		}
		
		for(int i = 0; i < optionList.size(); i++) {
			selectByIndex(element, i);
		}
		
		return true;
	}

	public boolean deselectByText(Element element, String text)
	{
		Select select = createSelect(element);
		if(select != null)
		{
			select.deselectByVisibleText(text);
			return true;
		}

		return false;
	}

	public boolean deselectByValue(Element element, String value)
	{
		Select select = createSelect(element);
		if(select != null)
		{
			select.deselectByValue(value);
			return true;
		}

		return false;
	}

	public boolean deselectByIndex(Element element, int index)
	{
		Select select = createSelect(element);
		if(select != null)
		{
			select.deselectByIndex(index);
			return true;
		}

		return false;
	}

	public boolean deselectAll(Element element)
	{
		Select select = createSelect(element);
		if(select != null)
		{
			select.deselectAll();
			return true;
		}

		return false;
	}

	public boolean isMultiple(Element element)
	{
		Select select = createSelect(element);
		if(select != null)
		{
			return select.isMultiple();
		}

		return false;
	}

	public List<Element> getOptions(Element element)
	{
		return null;
	}

	public List<Element> getSelectedOptions(Element element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEnabled(Element element)
	{
		return searchStrategyUtils.findStrategy(WebElement.class, element).search(element).isEnabled();
	}

	public boolean isHidden(Element element)
	{
		return !searchStrategyUtils.findStrategy(WebElement.class, element).search(element).isDisplayed();
	}
	
	private Select createSelect(Element element)
	{
		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, element).search(element);
		if (webEle != null)
		{
			return new Select(webEle);
		}

		return null;
	}

}
