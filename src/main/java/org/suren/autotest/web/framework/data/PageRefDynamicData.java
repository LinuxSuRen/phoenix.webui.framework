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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.surenpi.autotest.datasource.DynamicData;
import com.surenpi.autotest.utils.StringUtils;
import com.surenpi.autotest.webui.Page;
import com.surenpi.autotest.webui.ui.AbstractElement;
import com.surenpi.autotest.webui.ui.Text;

/**
 * Page对象之间的属性数据引用
 * @author <a href="http://surenpi.com">suren</a>
 */
@Component
public class PageRefDynamicData implements DynamicData, ApplicationContextAware
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PageRefDynamicData.class);
	
	private ApplicationContext context;
	
	@Override
	public String getValue(String orginData)
	{
		if(StringUtils.isBlank(orginData))
		{
			return null;
		}
		
		String[] orginArrayData = orginData.split("\\.", 2);
		
		String clsName = orginArrayData[0];
		String fieldName = orginArrayData[1];
		
		clsName = StringUtils.uncapitalize(clsName); //spring的bean名称是首字母小写的规则
		
		Page page = null;
		
		try
		{
			page = context.getBean(clsName, Page.class);
		}
		catch(NoSuchBeanDefinitionException e)
		{
			LOGGER.error("Can not found page class by name [{}].", clsName);
		}
		catch(BeanNotOfRequiredTypeException e)
		{
			LOGGER.error("The class [{}] is not a Page type.", clsName);
		}
		
		if(page == null)
		{
			throw new RuntimeException("Can not found page class!");
		}

		try
		{
			Method readMethod = new PropertyDescriptor(
					fieldName, page.getClass()).getReadMethod();
			
			Object result = readMethod.invoke(page);
			if(result != null && AbstractElement.class.isAssignableFrom(result.getClass()))
			{
				if(result instanceof Text)
				{
					return ((Text) result).getValue();
				}
				else
				{
					throw new RuntimeException("Not support field type [" + result.getClass() + "].");
				}
			}
		}
		catch (IntrospectionException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getType()
	{
		return "page_ref";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException
	{
		this.context = applicationContext;
	}
}
