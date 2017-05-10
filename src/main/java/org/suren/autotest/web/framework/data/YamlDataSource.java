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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;

import org.suren.autotest.web.framework.page.Page;
import org.yaml.snakeyaml.Yaml;

/**
 * YAML格式的数据源
 * @author suren
 * @date 2017年5月10日 下午2:22:30
 */
public class YamlDataSource implements DataSource
{
	private URL url;

	@Override
	public void setGlobalMap(Map<String, Object> globalMap)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getGlobalMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean loadData(DataResource resource, Page page)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadData(DataResource resource, int row, Page page)
	{
		try
		{
			url = resource.getUrl();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if(url == null)
		{
			return false;
		}
		
		try(InputStream input = url.openStream())
		{
			parse(input, page);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return true;
	}

	/**
	 * @param input
	 */
	private void parse(InputStream input, Page targetPage)
	{
		Yaml yaml = new Yaml();
		
		Map map = (Map) yaml.load(input);
		
		String pageName = targetPage.getClass().getName();
		if(map.containsKey("package") && map.get("package") != null)
		{
			String pkg = map.get("package").toString();
			
			pageName = pageName.replace(pkg + ".", "");
		}
		
		if(!map.containsKey(pageName))
		{
			return;
		}
		
		Object fieldsObj = map.get(pageName);
		if(fieldsObj instanceof Map)
		{
			Map fieldMap = (Map) fieldsObj;
			pageParse(fieldMap, targetPage);
		}
	}

	/**
	 * @param fieldMap
	 * @param targetPage
	 */
	private void pageParse(Map fieldMap, Page targetPage)
	{
		Class<? extends Page> targetPageCls = targetPage.getClass();
		Field[] decFields = targetPageCls.getDeclaredFields();
		for(Field field : decFields)
		{
			String name = field.getName();
			Object data = fieldMap.get(name);
			
			
			try
			{
				try
				{
					setValue(field, targetPage, data);
				}
				catch (NoSuchMethodException | SecurityException
						| InvocationTargetException e)
				{
					e.printStackTrace();
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param field
	 * @param targetPage
	 * @param data
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	private void setValue(Field field, Page targetPage, Object data) throws IllegalArgumentException,
		IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException
	{
		field.setAccessible(true);
		Object fieldObj = field.get(targetPage);
		if(fieldObj == null)
		{
			return;
		}
		
		Method method = fieldObj.getClass().getMethod("setValue", String.class);
		
		method.invoke(fieldObj, data.toString());
	}

}
