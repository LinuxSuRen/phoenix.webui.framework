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
}
