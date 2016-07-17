package org.suren.autotest.web.framework.selenium.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * 根据查找元素的优先级（{@link PrioritySearchStrategy}）进行便历查找， 找不到返回null
 * 
 * @see PrioritySearchStrategy
 * @see ZoneSearchStrategy
 * @author suren
 * @date Jul 16, 2016 7:20:54 PM
 */
@Component("cyleSearchStrategy")
public class CyleSearchStrategy implements ElementSearchStrategy<WebElement>
{

	@Autowired
	private SeleniumEngine engine;

	public WebElement search(Element element)
	{
		List<By> byList = new ArrayList<By>();

		if (StringUtils.isNotBlank(element.getId()))
		{
			byList.add(By.id(element.getId()));
		}
		else if (StringUtils.isNoneBlank(element.getCSS()))
		{
			byList.add(By.className(element.getCSS()));
		}
		else if (StringUtils.isNotBlank(element.getXPath()))
		{
			byList.add(By.xpath(element.getXPath()));
		}
		else if (StringUtils.isNotBlank(element.getLinkText()))
		{
			byList.add(By.linkText(element.getLinkText()));
		}
		else if (StringUtils.isNoneBlank(element.getPartialLinkText()))
		{
			byList.add(By.partialLinkText(element.getPartialLinkText()));
		}
		else if (StringUtils.isNotBlank(element.getTagName()))
		{
			byList.add(By.tagName(element.getTagName()));
		}

		return cyleFindElement(byList);
	}

	private WebElement cyleFindElement(List<By> byList)
	{
		WebElement webEle = null;

		for (By by : byList)
		{
			try
			{
				webEle = findElement(by);
			}
			catch (NoSuchElementException e)
			{
			}

			if (webEle != null)
			{
				return webEle;
			}
		}

		return null;
	}

	private WebElement findElement(By by)
	{
		return engine.getDriver().findElement(by);
	}

}
