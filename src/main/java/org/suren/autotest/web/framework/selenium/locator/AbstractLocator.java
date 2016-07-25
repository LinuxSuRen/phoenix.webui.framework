/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import org.suren.autotest.web.framework.core.Locator;
import org.suren.autotest.web.framework.core.LocatorAware;

/**
 * @author suren
 * @date 2016年7月25日 下午12:43:15
 */
public abstract class AbstractLocator implements Locator, LocatorAware
{
	private String value;

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(String value)
	{
		this.value = value;
	}
}
