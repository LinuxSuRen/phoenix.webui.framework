/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 有关操作系统的一些工具方法
 * @author suren
 * @date 2016年12月13日 下午9:55:37
 */
public class OSUtil
{
	/**
	 * 根据系统环境变量PROCESSOR_ARCHITECTURE来判断
	 * @return 包含64返回true，否则返回false
	 */
	public static boolean is64Bit()
	{
		String osArch = System.getenv("PROCESSOR_ARCHITECTURE");
		if(StringUtils.isNotBlank(osArch))
		{
			return osArch.contains("64");
		}
		else
		{
			return false;
		}
	}
}
