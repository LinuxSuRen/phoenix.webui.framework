/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.util.CommonNumberUtil;
import org.suren.autotest.web.framework.util.IDCardUtil;
import org.suren.autotest.web.framework.util.StringUtil;

/**
 * 简单的动态数据实现
 * @author suren
 * @date 2017年1月4日 下午12:33:01
 */
@Component
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
		
		if(value.contains("${email}"))
		{
			value = value.replace("${email}", StringUtil.email());
		}
		
		if(value.contains("${phone}"))
		{
			value = value.replace("${phone}", CommonNumberUtil.phoneNumber());
		}
		
		if(value.contains("${postcode}"))
		{
			value = value.replace("${postcode}", CommonNumberUtil.postCode());
		}
		
		return value;
	}

}
