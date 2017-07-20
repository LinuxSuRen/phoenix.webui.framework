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

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.surenpi.autotest.datasource.DynamicData;

/**
 * @author suren
 * @since 2017年1月11日 下午1:32:02
 */
@Component
public class CallbackDynamicData implements DynamicData, ApplicationContextAware
{
	private static final String DYNAMIC_DATA = "dynamic.data";
	
	private ApplicationContext context;
	private Map<String, Object> data;

	@Override
	public String getValue(String orginData)
	{
		String type = dataPrepare();
		
		Map<String, DynamicData> dynamicDataMap = context.getBeansOfType(DynamicData.class);
		for(DynamicData dynamicData : dynamicDataMap.values())
		{
			if(dynamicData == this)
			{
				continue;
			}
			
			if(dynamicData.getType().equals(type))
			{
				dynamicData.setData(data);
				return dynamicData.getValue(orginData);
			}
		}
		
		return orginData;
	}
	
	/**
	 * 默认参数准备
	 */
	private String dataPrepare()
	{
		if(data == null)
		{
			data = new HashMap<String, Object>();
		}
		
		if(!data.containsKey(DYNAMIC_DATA))
		{
			data.put(DYNAMIC_DATA, "freemarker");
		}
		
		return (String) data.get(DYNAMIC_DATA);
	}

	@Override
	public String getType()
	{
		return "callback";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
		this.data = data;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException
	{
		this.context = context;
	}

}
