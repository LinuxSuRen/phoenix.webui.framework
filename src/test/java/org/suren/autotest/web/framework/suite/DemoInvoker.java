/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.suite;

import org.suren.autotest.web.framework.page.LoginPage;
import org.suren.autotest.web.framework.settings.SettingUtil;

/**
 * @author suren
 * @date 2016年12月11日 上午11:09:31
 */
public class DemoInvoker
{
	public static void execute(SettingUtil settingUtil)
	{
		System.out.println("I'am a demo invoker!" + settingUtil.getPage(LoginPage.class));
	}
}
