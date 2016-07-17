package org.suren.autotest.web.framework.test;

import org.suren.autotest.web.framework.baidu.BaiduHomePage;
import org.suren.autotest.web.framework.settings.SettingUtil;

public class BaiduTest {

	public static void main(String[] args) throws Exception {
		SettingUtil util = new SettingUtil();
		
		try {
			util.readFromClassPath("baidu_website.xml");
			util.initData();
			
			BaiduHomePage homePage = util.getPage(BaiduHomePage.class);
			
			homePage.open();
			
			homePage.getToLoginBut().click();
			
			homePage.getUserNameText().fillValue();
			homePage.getPasswdText().fillValue();
			homePage.getLoginBut().click();
			
			Thread.sleep(8000);
			
			homePage.close();
		} finally {
		}
	}

}
