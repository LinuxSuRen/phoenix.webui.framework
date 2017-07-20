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
 * @since 2017年1月7日 下午6:59:15
 */
public class StringUtilsTest
{
	@Test
	public void email()
	{
		String server = "126.com";
		
		String email = StringUtils.email(server);
		
		Assert.assertNotNull(email);
		Assert.assertTrue(email.endsWith(server));
		
		email = StringUtils.email();
		Assert.assertNotNull(email);
	}
	
	@Test
	public void isBlank()
	{
		Assert.assertTrue(StringUtils.isBlank(""));
		Assert.assertTrue(StringUtils.isBlank(null));
		Assert.assertTrue(!StringUtils.isBlank("gril"));
	}
	
	@Test
	public void isNotBlank()
	{
		Assert.assertTrue(StringUtils.isNotBlank("gril"));
	}
	
	@Test
	public void isAnyBlank()
	{
		Assert.assertTrue(StringUtils.isAnyBlank("", "a"));
		Assert.assertTrue(StringUtils.isAnyBlank(null, "a"));
	}
}
