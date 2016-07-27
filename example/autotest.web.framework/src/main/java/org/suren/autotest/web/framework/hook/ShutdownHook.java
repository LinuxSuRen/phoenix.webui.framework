/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.hook;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suren.autotest.web.framework.settings.SettingUtil;

/**
 * 为了防止在程序意外关闭或者是用户没有显示地调用关闭浏览器的api导致的资源没有释放
 * @author suren
 * @date 2016年7月27日 下午3:49:12
 */
public class ShutdownHook extends Thread
{
	private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);
	
	private SettingUtil settingUtil;
	
	/**
	 * @param context
	 */
	public ShutdownHook(SettingUtil settingUtil)
	{
		this.settingUtil = settingUtil;
		logger.info("egnine close hook already regist.");
	}

	@Override
	public void run()
	{
		logger.info("prepare to execute engine close operation.");
		
		try
		{
			settingUtil.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		logger.info("engine closed successful.");
	}

}
