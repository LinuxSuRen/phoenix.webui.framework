package org.suren.autotest.web.framework.test;

import org.suren.autotest.web.framework.settings.SettingUtil;
import org.suren.autotest.web.framework.surenpi.HomePage;

public class SuRenPiTest {

	public static void main(String[] args) throws Exception {
		SettingUtil util = new SettingUtil();
		
		try {
			util.readFromClassPath("surenpi_website.xml");
			
			HomePage homePage = util.getPage(HomePage.class);
			
			System.out.println(homePage);
			
			homePage.open();
			homePage.getHomeBut().click();
			homePage.close();
		} finally {
		}
	}

}
