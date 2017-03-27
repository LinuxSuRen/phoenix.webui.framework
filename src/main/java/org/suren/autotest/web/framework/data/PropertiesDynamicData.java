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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 属性文件动态数据
 * @author suren
 * @date 2017年3月26日 下午12:52:28
 */
@Component
public class PropertiesDynamicData implements DynamicData
{
	private static final Logger logger = LoggerFactory.getLogger(PropertiesDynamicData.class);
	
	private String path = "dynamic.data.properties";
	private Properties pro = new Properties();

	@Override
	public String getValue(final String orginData)
	{
		String data = orginData;
		for(Object key : pro.keySet())
		{
			String strKey = key.toString();
			String pattern = ("${" + strKey + "}");
			
			data = data.replace(pattern, pro.getProperty(strKey));
		}
		
		return data;
	}

	@Override
	public String getType()
	{
		return "properties";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

	@PostConstruct
	public void init() throws IOException
	{
		ClassLoader loader = PropertiesDynamicData.class.getClassLoader();
		try(InputStream input = loader.getResourceAsStream(path))
		{
			if(input == null)
			{
				logger.warn(String.format("Can not found file [%s] is class path.", path));
				return;
			}
			
			pro.load(input);
		}
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

}
