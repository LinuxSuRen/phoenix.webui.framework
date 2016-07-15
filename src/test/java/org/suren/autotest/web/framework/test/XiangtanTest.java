/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.test;

import org.suren.autotest.web.framework.page.BasicInformaPage;
import org.suren.autotest.web.framework.page.HomePage;
import org.suren.autotest.web.framework.page.LoginPage;
import org.suren.autotest.web.framework.page.SafetyProductPermitPage;
import org.suren.autotest.web.framework.page.SystemSelectPage;
import org.suren.autotest.web.framework.settings.SettingUtil;

/**
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT
 * 2016年7月1日
 */
public class XiangtanTest {

	/**
	 * TODO
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		SettingUtil settingUtil = new SettingUtil();
		settingUtil.read("D:/Gboat-Toolkit-Suit/workspace/Glodon.AutoDemo/src/main/resources/basicinformation.xml");
		
		LoginPage loginPage = settingUtil.getPage(new LoginPage());
		SystemSelectPage systemSelectPage = settingUtil.getPage(new SystemSelectPage());
		HomePage homePage = settingUtil.getPage(new HomePage());
		BasicInformaPage basicInformaPage=settingUtil.getPage(new BasicInformaPage());
		SafetyProductPermitPage SafetyProductPermitPage=settingUtil.getPage(new SafetyProductPermitPage());
		//选择交易平台
		systemSelectPage.open();
		systemSelectPage.getSysButton().click();
		//登录
		loginPage.getLoginNameText().fillValue();
		loginPage.getPasswdText().fillValue();
		loginPage.getLoginButton().click();
		
		//点击【企业信息维护系统】
		Thread.sleep(3000);
		homePage.getGfmButton().click();
		
        //录入基本信息-基本信息
		Thread.sleep(3000);
		basicInformaPage.getEnterpriseName().fillValue();//企业名称
		basicInformaPage.getRegisterAddrP().selectByText("江苏省");
		basicInformaPage.getRegisterAddrC().selectByText("南京市");
		basicInformaPage.getRegisterAddrQ().selectByText("玄武区");
		basicInformaPage.getRegisterAddr().fillValue();//注册地址
		basicInformaPage.getRegisterCode().fillValue();//注册地址邮编
		basicInformaPage.getRegisterDate().click();//企业注册时间
		basicInformaPage.getBank().fillValue();//开户银行
		basicInformaPage.getBankNum().fillValue();//基本账户号
		basicInformaPage.getOrganizationCodeA().click();//是否三证合一-是
		basicInformaPage.getBusinessDirec().click();//业务方向-建设工程
		basicInformaPage.getBusinessTypeSelect().click();//企业类别-施工
		
		//录入基本信息-安全生产许可证
		SafetyProductPermitPage.getPermitNo().fillValue();
	}

}
