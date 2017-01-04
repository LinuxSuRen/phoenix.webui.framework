/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author suren
 * @date 2017年1月4日 上午10:25:36
 */
public class IDCardUtilTest
{
	@Test
	public void test()
	{
		String code = IDCardUtil.generate();
		
		Assert.assertNotNull(code);
		
		System.out.println(code);
	}
}
