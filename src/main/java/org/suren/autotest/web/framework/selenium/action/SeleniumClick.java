/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium.action;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.Iterator;
import java.util.Set;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ClickAble;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.core.ui.FileUpload;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

/**
 * 通过Selenium实现点击（单击、双击）
 * 
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
public class SeleniumClick implements ClickAble
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumClick.class);
	
	@Autowired
	private SeleniumEngine			engine;
	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;

	@Override
	public void click(Element ele)
	{
		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele);
		if(webEle == null)
		{
			logger.warn("can not found element.");
			return;
		}
		
		try
		{
			if(!(ele instanceof FileUpload) && !(engine.getDriver() instanceof RemoteWebDriver))
			{
				Dimension size = webEle.getSize();
				Point loc = webEle.getLocation();
				int toolbarHeight = engine.getToolbarHeight();
				int x = size.getWidth() / 2 + loc.getX();
				int y = size.getHeight() / 2 + loc.getY() + toolbarHeight;
				
				new Robot().mouseMove(x, y);
			}
			
			String tagName = webEle.getTagName();
			String targetAttr = webEle.getAttribute("target");
			webEle.click();
			if("a".equals(tagName) && "_blank".equals(targetAttr))
			{
				WebDriver driver = engine.getDriver();
				Set<String> handlers = driver.getWindowHandles();
				Iterator<String> it = handlers.iterator();
				while(it.hasNext())
				{
					String name = it.next();
					
					driver.switchTo().window(name);
				}
			}
		}
		catch(WebDriverException e)
		{
			if(e.getMessage().contains("is not clickable at point"))
			{
				new Actions(engine.getDriver()).moveToElement(webEle, -50, -50).perform();
				
//				WebDriverWait wait = new WebDriverWait(engine.getDriver(), 30);
				
				((JavascriptExecutor) engine.getDriver()).executeScript("arguments[0].scrollIntoView();", webEle, -50, -50);
				
				webEle.click();
			}
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void dbClick(Element ele)
	{
		Actions actions = new Actions(engine.getDriver());
		actions.doubleClick(searchStrategyUtils.findStrategy(WebElement.class, ele).search(ele));
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
