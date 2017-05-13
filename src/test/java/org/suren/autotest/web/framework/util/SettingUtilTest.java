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

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.Test;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.HomePage;
import org.suren.autotest.web.framework.page.LoginPage;
import org.suren.autotest.web.framework.settings.SettingUtil;
import org.xml.sax.SAXException;

/**
 * @author suren
 * @date 2017年5月13日 下午12:06:10
 */
public class SettingUtilTest
{
	@Test
	public void simple() throws IOException, DocumentException, SAXException
	{
		try(SettingUtil util = new SettingUtil())
		{
			util.readFromClassPath("elements/xml/maimai.xml");
			util.initData();
			
			HomePage homePage = util.getPage(HomePage.class);
			Assert.assertNotNull(homePage);

			Assert.assertNotNull(homePage.getUrl());
			Assert.assertTrue("起始页地址不合法", homePage.paramTranslate(homePage.getUrl()).startsWith("http"));
			homePage.open();
			Button toLoginBut = homePage.getToLoginBut();
			Assert.assertNotNull(toLoginBut);
			toLoginBut.click();
			
			LoginPage loginPage = util.getPage(LoginPage.class);
			Assert.assertNotNull(loginPage);
			
			Text phone = loginPage.getPhone();
			Assert.assertNotNull(phone);
			phone.fillNotBlankValue();
		}
	}
}
