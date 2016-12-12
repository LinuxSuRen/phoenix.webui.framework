/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.invoker;

import java.io.IOException;

/**
 * 执行本地命令的外部执行类
 * @author suren
 * @date 2016年12月12日 下午12:17:25
 */
public class OSInvoker
{
	public static void exec(String cmd)
	{
		try
		{
			Runtime.getRuntime().exec(cmd);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
