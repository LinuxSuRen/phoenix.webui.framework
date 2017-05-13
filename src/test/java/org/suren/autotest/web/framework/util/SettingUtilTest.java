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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.HomePage;
import org.suren.autotest.web.framework.page.LoginPage;
import org.suren.autotest.web.framework.page.MenuPage;
import org.suren.autotest.web.framework.page.SSIPPage;
import org.suren.autotest.web.framework.settings.SettingUtil;
import org.xml.sax.SAXException;

/**
 * 关于框架加载元素定位和数据部分的单元测试
 * @author suren
 * @date 2017年5月13日 下午12:06:10
 */
public class SettingUtilTest
{
	private SettingUtil util;
	
	/**
	 * 加载元素定位信息，初始化数据源
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SAXException
	 */
	@Before
	public void before() throws IOException, DocumentException, SAXException
	{
		util = new SettingUtil();
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

	/**
	 * 测试Page类加载
	 * @throws SAXException 
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	@Test
	public void pageLoad() throws IOException, DocumentException, SAXException
	{
		util.readFromClassPath("elements/xml/maimai.xml");
		util.initData();
		
		Assert.assertNotNull(util.getPage(HomePage.class));
		Assert.assertNotNull(util.getPage(LoginPage.class));
		Assert.assertNotNull(util.getPage(MenuPage.class));
		Assert.assertNotNull(util.getPage(SSIPPage.class));
	}
	
	/**
	 * 测试简单流程，通过编码的方式输入数据
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SAXException
	 */
	@Test
	public void simple() throws IOException, DocumentException, SAXException
	{
		util.readFromClassPath("elements/xml/maimai.xml");
		util.initData();
		
		HomePage homePage = util.getPage(HomePage.class);
		Assert.assertNotNull(homePage);

		Assert.assertNotNull(homePage.getUrl());
		Assert.assertTrue("起始页地址不合法",
				homePage.paramTranslate(homePage.getUrl()).startsWith("http"));
		homePage.open();
		Button toLoginBut = homePage.getToLoginBut();
		Assert.assertNotNull(toLoginBut);
		toLoginBut.click();
		
		LoginPage loginPage = util.getPage(LoginPage.class);
		Assert.assertNotNull(loginPage);
		
		Text phone = loginPage.getPhone();
		Assert.assertNotNull(phone);
		phone.fillValue("18211145623");
	}

	/**
	 * 测试简单流程，数据在数据源（xml格式）中
	 * @throws SAXException 
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	@Test
	public void dataSource() throws IOException, DocumentException, SAXException
	{
		util.readFromClassPath("elements/xml/maimai.xml");
		util.initData();
		
		HomePage homePage = util.getPage(HomePage.class);
		Assert.assertNotNull(homePage);

		Assert.assertNotNull(homePage.getUrl());
		Assert.assertTrue("起始页地址不合法",
				homePage.paramTranslate(homePage.getUrl()).startsWith("http"));
		homePage.open();
		Button toLoginBut = homePage.getToLoginBut();
		Assert.assertNotNull(toLoginBut);
		toLoginBut.click();
		
		LoginPage loginPage = util.getPage(LoginPage.class);
		Assert.assertNotNull(loginPage);
		
		Text phone = loginPage.getPhone();
		Assert.assertNotNull(phone);
		Assert.assertNotNull("数据未从数据源中加载", phone.getValue());
		phone.fillNotBlankValue();
	}
	
	/**
	 * 测试excel格式的数据源加载
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SAXException
	 */
	@Test
	public void excelDataSource() throws IOException, DocumentException, SAXException
	{
		util.readFromClassPath("elements/xml/maimai_exceldata.xml");
		util.initData();
		
		HomePage homePage = util.getPage(HomePage.class);
		Assert.assertNotNull(homePage);

		Assert.assertNotNull(homePage.getUrl());
		Assert.assertTrue("起始页地址不合法",
				homePage.paramTranslate(homePage.getUrl()).startsWith("http"));
		homePage.open();
		Button toLoginBut = homePage.getToLoginBut();
		Assert.assertNotNull(toLoginBut);
		toLoginBut.click();
		
		LoginPage loginPage = util.getPage(LoginPage.class);
		Assert.assertNotNull(loginPage);
		
		Text phone = loginPage.getPhone();
		Assert.assertNotNull(phone);
		Assert.assertNotNull("数据未从数据源中加载", phone.getValue());
		phone.fillNotBlankValue();
	}
}
