/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import java.util.Iterator;
import java.util.Map;

/**
 * @author suren
 * @date 2016年11月26日 上午10:22:04
 */
public class StringUtils
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
	
    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtils.isBlank(cs);
    }
    
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

	/**
	 * @param hostType
	 * @param hostValue
	 * @return
	 */
	public static boolean isAnyBlank(String hostType, String hostValue)
	{
		return StringUtils.isBlank(hostValue) || StringUtils.isBlank(hostType);
	}

	/**
	 * 把字符串转化为首字母小写
	 * @param clsName
	 * @return
	 */
    public static String uncapitalize(String str)
    {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) 
        {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
}
