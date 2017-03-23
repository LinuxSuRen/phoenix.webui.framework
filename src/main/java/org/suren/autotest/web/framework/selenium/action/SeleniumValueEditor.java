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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.AdvanceValueEditor;
import org.suren.autotest.web.framework.core.action.ValueEditor;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;
import org.suren.autotest.web.framework.util.StringUtils;

/**
 * 给文本框中填入值
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
public class SeleniumValueEditor implements ValueEditor, AdvanceValueEditor
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumValueEditor.class);

	@Autowired
	private SeleniumEngine			engine;
	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;

	@Override
	public Object getValue(Element ele)
	{
		return searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele).getText();
	}

	/**
	 * 实现逻辑：点击文本框、清空文本框、填入值 
	  */
	@Override
	public void setValue(Element ele, Object value)
	{
		fillValue(ele, value, false);
	}

	@Override
	public void appendValue(Element ele, Object value)
	{
		fillValue(ele, value, true);
	}

	@Override
	public void fillNotBlankValue(Element ele, Object value)
	{
		if(value == null || StringUtils.isBlank(value.toString()))
		{
			throw new RuntimeException("Can not allow null or empty value!");
		}
		
		fillValue(ele, value, true);
	}
	
	/**
	 * @param ele
	 * @param value
	 * @param append 是否追加
	 */
	private void fillValue(Element ele, Object value, boolean append)
	{
		if(value == null)
		{
			value = "";
		}

		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele);
		if(webEle != null)
		{
			String readonlyAttr = webEle.getAttribute("readonly");
			if("true".equals(readonlyAttr))
			{
				logger.warn("{} is readonly, will do not call method setValue.", webEle.toString());
				return;
			}
			
			try
			{
				String valueStr = value.toString();
				
				webEle.click();
				if(!append)
				{
					webEle.clear();
				}
				webEle.sendKeys(value.toString());
				
				if("input".equals(webEle.getTagName()))
				{
					if(!valueStr.equals(webEle.getAttribute("value")))
					{
						webEle.click();
						if(!append)
						{
							webEle.clear();
						}
						webEle.sendKeys(value.toString());
					}
				}
			}
			catch(WebDriverException e)
			{
				if(e.getMessage().contains("is not clickable at point"))
				{
					((JavascriptExecutor) engine.getDriver()).executeScript("arguments[0].scrollIntoView();", webEle);

					webEle.click();
					if(!append)
					{
						webEle.clear();
					}
					webEle.sendKeys(value.toString());
				}
				else
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			logger.error(String.format("can not found element [%s].", ele));
		}
	}

	@Override
	public void submit(Element ele)
	{
		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele);
		webEle.submit();
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
