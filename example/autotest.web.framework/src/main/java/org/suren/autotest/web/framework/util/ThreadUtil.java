/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

/**
 * @author suren
 * @date 2016年11月25日 下午3:03:32
 */
public class ThreadUtil
{
	/**
	 * 不用处理异常的睡眠方法
	 * @param timeout
	 */
	public static void silentSleep(long timeout)
	{
		try
		{
			Thread.sleep(timeout);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
