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
import org.suren.autotest.web.framework.util.RandomUtils;
import org.suren.autotest.web.framework.util.StringUtils;

/**
 * 简单的动态数据实现
 * @author suren
 * @date 2017年1月4日 下午12:33:01
 */
@Component
public class SimpleDynamicData implements DynamicData
{
	private List<String> formatList = new ArrayList<String>();
	
	private String random = "${random-";
	
	public SimpleDynamicData()
	{
		formatList.add("yyyy-MM-dd");
		formatList.add("yyyy-MM-dd HH:mm:ss");
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
			value = value.replace("${email}", StringUtils.email());
		}
		
		if(value.contains("${phone}"))
		{
			value = value.replace("${phone}", CommonNumberUtil.phoneNumber());
		}
		
		if(value.contains("${postcode}"))
		{
			value = value.replace("${postcode}", CommonNumberUtil.postCode());
		}
		
		if(value.contains(random))
		{
			value = parseRandomParam(value);
		}
		
		return value;
	}
	
	private String parseRandomParam(String randomParam)
	{
		if(randomParam.contains(random))
		{
			int index = -1;
			while((index = randomParam.indexOf(random)) != -1)
			{
				int numIndex = index + random.length();
				int numEndIndex = randomParam.indexOf("}", numIndex);
				
				int num = Integer.parseInt(randomParam.substring(numIndex, numEndIndex));
				
				num = RandomUtils.nextInt(num);
				
				randomParam = randomParam.substring(0, index) + num + randomParam.substring(numEndIndex + 1);
			}
		}
		
		return randomParam;
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
