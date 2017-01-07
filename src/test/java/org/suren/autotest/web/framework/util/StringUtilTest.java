/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author suren
 * @date 2017年1月7日 下午6:59:15
 */
public class StringUtilTest
{
	@Test
	public void email()
	{
		String server = "126.com";
		
		String email = StringUtil.email(server);
		
		System.out.println(email);
		Assert.assertNotNull(email);
		Assert.assertTrue(email.endsWith(server));
		
		email = StringUtil.email();
		System.out.println(email);
		Assert.assertNotNull(email);
	}
}
