/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2016年7月27日 下午12:11:23
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumTextLocator extends AbstractLocator
{

	@Override
	public String getType()
	{
		return "byText";
	}

}
