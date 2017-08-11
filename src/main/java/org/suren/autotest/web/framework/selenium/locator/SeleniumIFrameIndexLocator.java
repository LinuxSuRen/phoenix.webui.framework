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

package org.suren.autotest.web.framework.selenium.locator;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * iframe下标定位器
 * @author suren
 * @since 2016年7月27日 下午4:43:27
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumIFrameIndexLocator extends AbstractLocator<WebElement>
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumIFrameIndexLocator.class);

	@Autowired
	private SeleniumEngine engine;
	
	@Override
	public String getType()
	{
		return "byIFrameIndex";
	}

	@Override
	public By getBy()
	{
		return null;
	}

	@Override
	public WebElement findElement(SearchContext driver)
	{
		if(!(driver instanceof WebDriver))
		{
			throw new IllegalArgumentException("Argument must be instanceof WebDriver.");
		}
		
		int index = -1;
		
		try
		{
			index = Integer.parseInt(getValue());
		} catch(NumberFormatException e)
		{
			throw new IllegalArgumentException("Frame index value must be integer.", e);
		}
		
		if(index != -1)
		{
			WebDriver webDriver = (WebDriver) driver;
			
			webDriver = engine.turnToRootDriver(webDriver);
			if(!iframeWait(webDriver, getTimeout(), index))
			{
				webDriver.switchTo().frame(index);
			}
			
			logger.trace(String.format("current url [%s].", webDriver.getCurrentUrl()));
		}
		
		return null;
	}

}
