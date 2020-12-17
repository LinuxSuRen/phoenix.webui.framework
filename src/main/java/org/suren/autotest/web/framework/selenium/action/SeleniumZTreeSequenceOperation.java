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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

import com.surenpi.autotest.utils.StringUtils;
import com.surenpi.autotest.utils.ThreadUtil;
import com.surenpi.autotest.webui.action.SequenceAble;
import com.surenpi.autotest.webui.ui.Element;

/**
 * 利用Selenium来实现序列操作
 * @author linuxsuren
 */
@Component
public class SeleniumZTreeSequenceOperation implements SequenceAble
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumZTreeSequenceOperation.class);

	@Autowired
	private SeleniumEngine engine;
	
	@Override
	public void perform(Element element, List<String> actions)
	{
		if(CollectionUtils.isEmpty(actions) || actions.size() <= 1)
		{
			throw new RuntimeException("Error format.");
		}
		
		String parentXPath = actions.get(0);
		
		String xpath = parentXPath;
		WebDriver driver = engine.getDriver();
		
		WebElement parentEle = driver.findElement(By.xpath(xpath));
		
		int index = 1;
		for(; index < actions.size() - 1; index++)
		{
			xpath = String.format("%s/descendant::span[contains(text(),'%s')]", parentXPath, actions.get(index));
			
			logger.debug(xpath);
			parentEle = parentEle.findElement(By.xpath(xpath));
			String id = parentEle.getAttribute("id");
			logger.debug(id);
			
			if(StringUtils.isBlank(id))
			{
				return;
			}
			
			id = id.replace("span", "switch");
			logger.debug(id);
			WebDriverWait wait = new WebDriverWait(engine.getDriver(), 30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
			parentEle = driver.findElement(By.id(id));
			if(parentEle.getAttribute("class").endsWith("close"))
			{
				parentEle.click();
			}
			
			ThreadUtil.silentSleep(2000);
		}
		
		xpath = String.format("%s/descendant::span[contains(text(),'%s')]", parentXPath, actions.get(index));
		logger.debug(xpath);
		WebDriverWait wait = new WebDriverWait(engine.getDriver(), 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		parentEle.findElement(By.xpath(xpath)).click();
	}

	@Override
	public String getName()
	{
		return "ztree";
	}

	@Override
	public String getDescription()
	{
		return "基于ztree实现的连续操作";
	}

}
