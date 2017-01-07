/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import org.apache.commons.lang3.RandomUtils;

/**
 * 常见数字类型生成工具类
 * @author suren
 * @date 2017年1月7日 下午6:09:13
 */
public class CommonNumberUtil
{
	/**
	 * @return 随机的邮编
	 */
	public static String postCode()
	{
		StringBuffer buf = new StringBuffer();
		
		for(int i = 0; i < 6; i++)
		{
			buf.append(RandomUtils.nextInt(0, 10));
		}
		
		return buf.toString();
	}
	
	/**
	 * @return 随机生成中国的手机号码
	 */
	public static String phoneNumber()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("182");
		
		for(int i = 0; i < 8; i++)
		{
			buf.append(RandomUtils.nextInt(0, 10));
		}
		
		return buf.toString();
	}
}
