/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2016年7月27日 下午4:23:21
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumValueLocator extends AbstractSeleniumAttrLocator
{

	@Override
	public String getType()
	{
		return "byValue";
	}

	@Override
	public String getAttrName()
	{
		return "value";
	}
}
