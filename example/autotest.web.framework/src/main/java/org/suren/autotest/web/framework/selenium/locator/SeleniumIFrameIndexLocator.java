/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2016年7月27日 下午4:43:27
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumIFrameIndexLocator extends AbstractLocator
{

	@Override
	public String getType()
	{
		return "byIFrameIndex";
	}

}
