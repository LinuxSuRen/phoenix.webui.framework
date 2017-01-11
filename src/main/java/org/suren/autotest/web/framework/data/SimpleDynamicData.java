/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	private List<String> formatList = new ArrayList<String>();
	
	public SimpleDynamicData()
	{
		formatList.add("yyyy-MM-DD");
		formatList.add("yyyy-MM-DD HH:mm:ss");
	}

	@Override
	public String getValue(final String orginData)
	{
		String value = orginData;
		
		value = value.replace("${now}", String.valueOf(System.currentTimeMillis()));
		
		for(String format : formatList)
		{
			String param = "${now " + format + "}";
			if(value.contains(param))
			{
				value = value.replace(param, new SimpleDateFormat(format).format(new Date()));
			}
		}
		
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

	@Override
	public String getType()
	{
		return "simple";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

}
