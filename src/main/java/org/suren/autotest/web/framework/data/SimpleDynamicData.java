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

package org.suren.autotest.web.framework.data;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.util.CommonNumberUtil;
import org.suren.autotest.web.framework.util.IDCardUtil;
import org.suren.autotest.web.framework.util.RandomUtils;
import org.suren.autotest.web.framework.util.StringUtils;

import com.surenpi.autotest.datasource.DynamicData;
import com.surenpi.autotest.datasource.DynamicDateFormat;

/**
 * 简单的动态数据实现
 * @author suren
 * @date 2017年1月4日 下午12:33:01
 */
@Component
public class SimpleDynamicData implements DynamicData, DynamicDateFormat
{
	private Map<String, Object> globalData;
	
	private Set<String> formatSet = new HashSet<String>();

	private SimpleDateFormat dateFormat = new SimpleDateFormat();
	private String random = "${random-";
	
	public SimpleDynamicData()
	{
		formatSet.add("yyyy");
		formatSet.add("MM-dd");
		formatSet.add("yyyy-MM-dd");
		formatSet.add("MM-dd HH:mm");
		formatSet.add("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String getValue(final String orginData)
	{
		String value = orginData;
		
		Date nowDate = new Date();
		value = value.replace("${now}", String.valueOf(System.currentTimeMillis()));
		
		for(String format : formatSet)
		{
			String param = "${now " + format + "}";
			if(value.contains(param))
			{
				dateFormat.applyPattern(format);
				value = value.replace(param, dateFormat.format(nowDate));
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
		
		for(String key : globalData.keySet())
		{
			value = value.replace("${" + key + "}", globalData.get(key).toString());
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
		this.globalData = data;
	}

	@Override
	public Set<String> allFormats()
	{
		return Collections.unmodifiableSet(formatSet);
	}

	@Override
	public boolean addFormat(String format)
	{
		try
		{
			dateFormat.applyPattern(format);
			formatSet.add(format);
			return true;
		}
		catch(IllegalArgumentException | NullPointerException e)
		{
		}
		
		return false;
	}

}
