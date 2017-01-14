/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium.action;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ValueEditor;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

/**
 * 给文本框中填入值
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
public class SeleniumValueEditor implements ValueEditor
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
				webEle.clear();
				webEle.sendKeys(value.toString());
				
				if("input".equals(webEle.getTagName()))
				{
					if(!valueStr.equals(webEle.getAttribute("value")))
					{
						webEle.click();
						webEle.clear();
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
					webEle.clear();
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
