/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2016年7月29日 下午2:33:35
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumTagNameLocator extends AbstractLocator<WebElement>
{

	@Override
	public String getType()
	{
		return "byTagName";
	}

	@Override
	protected By getBy()
	{
		return By.tagName(getValue());
	}

}
