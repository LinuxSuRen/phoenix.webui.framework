/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.util;

import java.util.Iterator;
import java.util.Map;

/**
 * @author suren
 * @since 2016年11月26日 上午10:22:04
 */
public class StringUtils
{
	/**
	 * 把参数型的值进行转换
	 * @param data data
	 * @param paramPrefix paramPrefix
	 * @param value value
	 * @return value
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
	 * 
	 * @param namesize 邮箱@之前的字符最长随机长度
	 * @param servernamesize 邮箱@之后的字符最长随机长度
	 * @return 需要的随机Email地址
	 */
	public static String email( int namesize, int servernamesize) 
	{
		String name = RandomStringUtils.randomAlphabetic(randomtest(servernamesize));
		
		String[] sa = new String[3];
		sa[0]="com";
		sa[1]="cn";
		sa[2]="net";
		
		StringBuffer buf = new StringBuffer();
		String server=name+"."+sa[randomtest(sa.length)];

		buf.append(RandomStringUtils.randomAlphabetic(randomtest(namesize)));
		buf.append("@");
		buf.append(server);
		
		return buf.toString();
	}
	
	/**
	 * @return 随机的email地址 最大长度10个字符 如果需要指定 可以使用重载方法email(int namesize, int servernamesize)
	 */
	public static String email()
	{
		String name = RandomStringUtils.randomAlphabetic(randomtest(10));
		
		String[] sa = new String[3];
		sa[0]="com";
		sa[1]="cn";
		sa[2]="net";
		StringBuffer buf = new StringBuffer();
		String server=name+"."+sa[randomtest(sa.length)];

		buf.append(RandomStringUtils.randomAlphabetic(randomtest(10)));
		buf.append("@");
		buf.append(server);

		return buf.toString();
				
	}
	
	public static int randomtest(int max)
	{
		int x = 0;
		
		if (max > 0) 
		{
			x =  (int) (Math.random() * max);
			return x;
		}

		return 0;
	}

    /**
     * @param cs cs
     * @return 字符非空返回true
     */
    public static boolean isNotBlank(CharSequence cs)
    {
        return !StringUtils.isBlank(cs);
    }
    
    /**
     * @param cs cs
     * @return 空字符返回true
     */
    public static boolean isBlank(CharSequence cs)
    {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0)
        {
            return true;
        }
        
        for (int i = 0; i < strLen; i++)
        {
            if (Character.isWhitespace(cs.charAt(i)) == false)
            {
                return false;
            }
        }
        
        return true;
    }

	/**
	 * @param hostType hostType
	 * @param hostValue hostValue
	 * @return hostValue
	 */
	public static boolean isAnyBlank(String hostType, String hostValue)
	{
		return StringUtils.isBlank(hostValue) || StringUtils.isBlank(hostType);
	}

	/**
	 * 把字符串转化为首字母小写
	 * @param str str
	 * @return str
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

	/**
	 * 如果text为空，则返回defText
	 * @see #isBlank(CharSequence)
	 * @param text 待测试文本字符串
	 * @param defText 默认文本字符串
     * @return 字符串
     */
	public static String defaultIfBlank(String text, String defText)
	{
		if(StringUtils.isBlank(text))
		{
			return defText;
		}
		else
		{
			return text;
		}
	}

	/**
	 * @see #defaultIfBlank(String, String)
	 * @param text text
	 * @param numText 默认的数字文本
     * @return text
     */
	public static String defaultIfBlank(String text, int numText)
	{
		return defaultIfBlank(text, String.valueOf(numText));
	}

	/**
	 * @see #defaultIfBlank(String, String)
	 * @param text text
	 * @param numText 默认的数字文本
	 * @return text
	 */
	public static String defaultIfBlank(String text, long numText)
	{
		return defaultIfBlank(text, String.valueOf(numText));
	}
}
