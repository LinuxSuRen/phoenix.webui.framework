/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.util;

import org.junit.*;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.suren.autotest.web.framework.IgnoreReasonConstants;
import org.suren.autotest.web.framework.page.AnnotationPage;
import org.suren.autotest.web.framework.settings.DriverConstants;
import org.suren.autotest.web.framework.settings.SettingUtil;

import java.io.IOException;

/**
 * 测试使用注解配置的方式
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
	public void basicTest()
	{
		util.getEngine().setDriverStr(DriverConstants.DRIVER_HTML_UNIT);
		util.getEngine().init();

		AnnotationPage page = util.getPage(AnnotationPage.class);
		
		Assert.assertNotNull(page);
		Assert.assertNotNull(page.getUrl());

		Assert.assertNotNull(page.getToLoginBut());

		page.open();
		page.getToLoginBut().click();
	}

	@Test
	@Ignore(value = IgnoreReasonConstants.REAL_BROWSER)
	public void realTest()
	{
		util.getEngine().setDriverStr(DriverConstants.DRIVER_CHROME);
		util.getEngine().init();
		util.initData();

		AnnotationPage page = util.getPage(AnnotationPage.class);
		page.open();
		page.getToLoginBut().click();

		page.getPhoneText().fillNotBlankValue();
		page.getPasswordText().fillNotBlankValue();

		ThreadUtil.silentSleep(3000);
	}

	@Test
	public void encrypt()
	{
		System.out.println(EncryptorUtil.encryptWithBase64("123456"));
	}
	
	@After
	public void tearDown() throws IOException
	{
		util.close();
	}
}
