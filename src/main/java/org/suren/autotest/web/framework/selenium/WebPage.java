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

package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.surenpi.autotest.webui.Page;

/**
 * 用户定义一个页面时需要继承该类
 * @author linuxsuren
 */
public class WebPage extends Page
{
	@Autowired
	private SeleniumEngine	engine;

	@Override
	public void open()
	{
		String paramUrl = paramTranslate(getUrl());
		
		engine.openUrl(paramUrl);
		
		try
		{
			engine.computeToolbarHeight();
		}
		catch(UnhandledAlertException e)
		{
			Alert alert = engine.getDriver().switchTo().alert();
			if(alert != null)
			{
				alert.dismiss();

				engine.computeToolbarHeight();
			}
		}
	}

	@Override
	public void close()
	{
		engine.close();
	}

	@Override
	public void closeOthers()
	{
		String currentTitle = getTitle();
		String currentWinHandle = engine.getDriver().getWindowHandle();
		
		for(String winHandle : engine.getDriver().getWindowHandles())
		{
			WebDriver itemDrive = engine.getDriver().switchTo().window(winHandle);
			if(!itemDrive.getTitle().equals(currentTitle))
			{
				itemDrive.close();
			}
		}
		
		engine.getDriver().switchTo().window(currentWinHandle);
	}

	@Override
	public String getCurrentUrl()
	{
		return engine.getDriver().getCurrentUrl();
	}

	@Override
	public String getPageSource()
	{
		return engine.getDriver().getPageSource();
	}

	@Override
	public String getTitle()
	{
		return engine.getDriver().getTitle();
	}
}
