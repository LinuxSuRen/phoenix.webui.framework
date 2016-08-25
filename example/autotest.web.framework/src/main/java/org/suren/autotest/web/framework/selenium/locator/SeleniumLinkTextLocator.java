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
 * 超链接文本定位器
 * @author suren
 * @date 2016年7月25日 下午1:16:59
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumLinkTextLocator extends AbstractLocator<WebElement>
{

	@Override
	public String getType()
	{
		return "byLinkText";
	}

	@Override
	protected By getBy()
	{
		return By.linkText(getValue());
	}

}
