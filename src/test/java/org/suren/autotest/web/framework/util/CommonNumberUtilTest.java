/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author suren
 * @since 2017年1月7日 下午6:12:45
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
