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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.suren.autotest.web.framework.page.HomePage;
import org.suren.autotest.web.framework.page.LoginPage;
import org.suren.autotest.web.framework.settings.Phoenix;
import org.xml.sax.SAXException;

/**
 * 元素批量操作的工具类单元测试
 * @author suren
 * @since 2017年5月14日 下午9:43:38
 */
public class ElementUtilTest
{
	private Phoenix util;
	
	/**
	 * 加载元素定位信息，初始化数据源
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SAXException
	 */
	@Before
	public void before() throws IOException, DocumentException, SAXException
	{
		util = new Phoenix();
		util.readFromClassPath("elements/xml/maimai_chrome.xml");
		util.initData();
	}
	
	/**
	 * 释放资源
	 * @throws IOException
	 */
	@After
	public void after() throws IOException
	{
		util.close();
	}
	
	@Test
	public void buttonClick() throws IOException, DocumentException, SAXException
	{
		HomePage homePage = util.getPage(HomePage.class);
		homePage.open();
		ElementUtil.click(homePage.getToLoginBut());
	}
	
	@Test
	public void textFillValue()
	{
		HomePage homePage = util.getPage(HomePage.class);
		homePage.open();
		ElementUtil.click(homePage.getToLoginBut());
		
		LoginPage loginPage = util.getPage(LoginPage.class);
		ElementUtil.fillValue(loginPage.getPhone(), loginPage.getPassword());
	}
	
	@Test
	public void pageOperation()
	{
		HomePage homePage = util.getPage(HomePage.class);
		homePage.open();
		ElementUtil.click(homePage);
		
		LoginPage loginPage = util.getPage(LoginPage.class);
		ElementUtil.fillValue(loginPage);
		ElementUtil.click(loginPage);
	}
}
