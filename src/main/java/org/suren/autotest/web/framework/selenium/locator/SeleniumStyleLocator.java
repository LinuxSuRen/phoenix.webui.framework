/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;


/**
 * @author suren
 * @date 2016年7月29日 下午2:59:24
 */
public abstract class SeleniumStyleLocator extends SeleniumAttrLocator
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
