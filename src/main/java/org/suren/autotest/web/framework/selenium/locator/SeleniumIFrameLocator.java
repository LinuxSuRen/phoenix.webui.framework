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
 * @date 2016年7月25日 下午12:44:34
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumIFrameLocator extends AbstractLocator<WebElement>
{

	@Autowired
	private SeleniumEngine engine;
	
	@Override
	public String getType()
	{
		return "byIFrame";
	}

	@Override
	protected By getBy()
	{
		return null;
	}

	@Override
	public WebElement findElement(WebDriver driver)
	{
		driver = engine.turnToRootDriver(driver);
		
		iframeWait(driver, getTimeout(), getValue());
		
		return null;
	}

}
