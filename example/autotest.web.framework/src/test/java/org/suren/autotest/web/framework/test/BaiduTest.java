package org.suren.autotest.web.framework.test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

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
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("a", "");
		map.put("c", "");
		map.put("b", "");
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext())
		{
			System.out.println(it.next());
		}
	}

}
