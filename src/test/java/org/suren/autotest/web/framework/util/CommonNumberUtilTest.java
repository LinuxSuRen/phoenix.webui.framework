/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author suren
 * @date 2017年1月7日 下午6:12:45
 */
public class CommonNumberUtilTest
{
	@Test
	public void postCode()
	{
		String postCode = CommonNumberUtil.postCode();
		
		System.out.println(postCode);
		
		Assert.assertNotNull(postCode);
		Assert.assertTrue(postCode.length() == 6);
	}
	
	@Test 
	public void phoneNumber()
	{
		String phoneNumber = CommonNumberUtil.phoneNumber();
		
		System.out.println(phoneNumber);
		
		Assert.assertNotNull(phoneNumber);
		Assert.assertTrue(phoneNumber.length() == 11);
	}
}
