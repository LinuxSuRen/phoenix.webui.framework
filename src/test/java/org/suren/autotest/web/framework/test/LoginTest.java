/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.test;

import org.suren.autotest.web.framework.page.LoginPage;
import org.suren.autotest.web.framework.page.LogoutPage;
import org.suren.autotest.web.framework.settings.SettingUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest {
	private LoginPage loginPage;
	private LogoutPage logoutPage;
	
	public static void main(String[] args) {
		new LoginTest().init();
	}
	
	@BeforeClass
	void init() {
		SettingUtil settingUtil = new SettingUtil();
		try {
			settingUtil.read("D:/Gboat-Toolkit-Suit/workspace/Glodon.AutoDemo/src/main/resources/page.xml");
			
			loginPage = settingUtil.getPage(new LoginPage());
			
			logoutPage = settingUtil.getPage(new LogoutPage());
			
			loginPage.open();
			loginPage.getLoginNameText().fillValue();
			loginPage.getPasswdText().fillValue();
			loginPage.getLoginButton().click();
			
			Thread.sleep(6000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void login() {
		loginPage.getLoginButton().click();
	}
	
//	@Test
	void logout() {
		logoutPage.getLogoutButtton().click();
	}
}
