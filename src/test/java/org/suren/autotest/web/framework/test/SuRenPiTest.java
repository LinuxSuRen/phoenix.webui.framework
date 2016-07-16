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
			
			homePage.getArchivesSelector().selectByText(" 2015年五月  (15)");
			
			for(int i = 0; i < 8; i++) {
				homePage.getNextBut().click();
				
				Thread.sleep(1000);
			}
			
			Thread.sleep(3000);
			
			homePage.close();
		} finally {
		}
	}

}
