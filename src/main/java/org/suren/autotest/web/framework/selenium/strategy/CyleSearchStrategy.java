/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.selenium.strategy;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.util.StringUtils;

/**
 * 根据查找元素的优先级（{@link PrioritySearchStrategy}）进行便历查找， 找不到返回null
 * 
 * @see PrioritySearchStrategy
 * @see ZoneSearchStrategy
 * @author suren
 * @date Jul 16, 2016 7:20:54 PM
 */
@Component("cyleSearchStrategy")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CyleSearchStrategy implements ElementSearchStrategy<WebElement>
{

	@Autowired
	private SeleniumEngine engine;

	@Override
	public WebElement search(Element element)
	{
		List<By> byList = new ArrayList<By>();

		if (StringUtils.isNotBlank(element.getId()))
		{
			byList.add(By.id(element.getId()));
		}
		else if (StringUtils.isNotBlank(element.getCSS()))
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
		else if (StringUtils.isNotBlank(element.getPartialLinkText()))
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
