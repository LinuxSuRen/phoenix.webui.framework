/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import org.suren.autotest.web.framework.util.IDCardUtil;

/**
 * 简单的动态数据实现
 * @author suren
 * @date 2017年1月4日 下午12:33:01
 */
public class SimpleDynamicData implements DynamicData
{

	@Override
	public String getValue(final String orginData)
	{
		String value = orginData;
		
		value = value.replace("${now}", String.valueOf(System.currentTimeMillis()));
		
		if(value.contains("${id_card}"))
		{
			value = value.replace("${id_card}", IDCardUtil.generate());
		}
		
		return value;
	}

}
