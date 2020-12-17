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

package org.suren.autotest.web.framework.selenium.action;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.locator.SeleniumValueLocator;
import org.suren.autotest.web.framework.selenium.strategy.ParentElement;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

import com.surenpi.autotest.webui.action.CheckAble;
import com.surenpi.autotest.webui.core.ElementSearchStrategy;
import com.surenpi.autotest.webui.core.Locator;
import com.surenpi.autotest.webui.ui.Element;

/**
 * 复选框选择
 * @author linuxsuren
 */
@Component
public class SeleniumCheck implements CheckAble, ApplicationContextAware
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumCheck.class);
	
	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;
	
	private ApplicationContext context;

	@Override
	public void checkByText(Element element, String text)
	{
		checkByValue(element, text);
	}

	@Override
	public void checkByValue(Element element, String value)
	{
		WebElement parentWebEle = searchStrategyUtils.findStrategy(WebElement.class, element).search(element);
		if(parentWebEle == null)
		{
			logger.error(String.format("can not found element byText [%s].", value));
			return;
		}
		
		List<Locator> locatorList = element.getLocatorList();
		List<Locator> tmpList = new ArrayList<Locator>(locatorList);
		
		ElementSearchStrategy<WebElement> strategy = context.getBean("zoneSearchStrategy", ElementSearchStrategy.class);
		
		SeleniumValueLocator valueLocator = context.getBean(SeleniumValueLocator.class);
		valueLocator.setHostType("byTagName");
		valueLocator.setHostValue("input");
		
		locatorList.clear();
		locatorList.add(valueLocator);
		valueLocator.setValue(value);
		
		WebElement itemWebEle = null;
		try
		{
			if(strategy instanceof ParentElement)
			{
				((ParentElement) strategy).setParent(parentWebEle);
			}
			
			itemWebEle = strategy.search(element);
			if(itemWebEle != null)
			{
				if(!itemWebEle.isSelected())
				{
					itemWebEle.click();
				}
			}
		}
		catch(ElementNotVisibleException e)
		{
			e.printStackTrace();
			logger.error(String.format("Element [%s] click error, parent [%s], text [%s].",
					itemWebEle, element, value));
		}
		finally
		{
			//清空缓存
			locatorList.clear();
			locatorList.addAll(tmpList);
			
			if(strategy instanceof ParentElement)
			{
				((ParentElement) strategy).setParent(null);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException
	{
		this.context = applicationContext;
	}

}
