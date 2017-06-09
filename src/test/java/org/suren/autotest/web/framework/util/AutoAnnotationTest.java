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

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.suren.autotest.web.framework.page.AnnotationPage;
import org.suren.autotest.web.framework.settings.SettingUtil;

/**
 * @author suren
 * @date 2017年6月7日 下午7:10:12
 */
@Configuration
@ComponentScan(basePackages = "org.suren.autotest.web.webframework.page")
public class AutoAnnotationTest
{
	private SettingUtil util;
	
	@Before
	public void setUp()
	{
		util = new SettingUtil();
	}
	
	@Test
	public void test()
	{
		AnnotationPage page = util.getPage(AnnotationPage.class);
		
		Assert.assertNotNull(page);
		
		Assert.assertNotNull(page.getSaveBut());
	}
	
	@After
	public void tearDown() throws IOException
	{
		util.close();
	}
}
