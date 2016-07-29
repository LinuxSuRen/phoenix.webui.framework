/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * @author suren
 * @date 2016年7月27日 下午4:43:27
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumIFrameIndexLocator extends AbstractLocator<WebElement>
{

	@Autowired
	private SeleniumEngine engine;
	
	@Override
	public String getType()
	{
		return "byIFrameIndex";
	}

	@Override
	protected By getBy()
	{
		return null;
	}

	@Override
	public WebElement findElement(WebDriver driver)
	{
		int index = -1;
		
		try
		{
			index = Integer.parseInt(getValue());
		} catch(NumberFormatException e) {}
		
		if(index != -1)
		{
			driver = engine.turnToRootDriver(driver);
			
			iframeWait(driver, getTimeout(), index);
		}
		
		return super.findElement(driver);
	}

}
