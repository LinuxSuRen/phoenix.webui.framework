/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2016年7月29日 下午2:59:24
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumStyleLocator extends AbstractSeleniumAttrLocator
{

	@Override
	public String getType()
	{
		return "byStyle";
	}

	@Override
	public String getAttrName()
	{
		return "style";
	}

}
