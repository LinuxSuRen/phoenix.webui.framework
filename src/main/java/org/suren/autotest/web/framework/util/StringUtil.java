/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author suren
 * @date 2016年11月26日 上午10:22:04
 */
public class StringUtil
{
	/**
	 * 把参数型的值进行转换
	 * @param data
	 * @param paramPrefix
	 * @param value
	 * @return
	 */
	public static String paramTranslate(Map<String, Object> data,
			String paramPrefix, String value)
	{
		String result = value;
		
		Iterator<String> dataIt = data.keySet().iterator();
		while(dataIt.hasNext())
		{
			String param = dataIt.next();
			if(!param.startsWith(paramPrefix))
			{
				continue;
			}
			
			Object paramVal = data.get(param);
			if(paramVal != null)
			{
				result = result.replace("${" + param + "}", paramVal.toString());
			}
		}
		
		return result;
	}
	
	/**
	 * @param server 给定邮件服务器地址
	 * @return 随机的email地址
	 */
	public static String email(String server)
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append(RandomStringUtils.randomAlphabetic(3));
		buf.append("@");
		buf.append(server);
		
		return buf.toString();
	}
	
	/**
	 * @return 随机的email地址
	 */
	public static String email()
	{
		String server = "qq.com";
		
		return email(server);
	}
}
