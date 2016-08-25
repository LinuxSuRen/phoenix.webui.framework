/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * id定位器
 * @author suren
 * @date 2016年7月25日 上午8:11:27
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumIDLocator extends AbstractLocator<WebElement>
{
	@Autowired
	private SeleniumEngine engine;
	
	@Override
	public String getType()
	{
		return "byId";
	}

	@Override
	protected By getBy()
	{
		return By.id(getValue());
	}
}
