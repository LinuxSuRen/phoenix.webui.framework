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

package org.suren.autotest.web.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.surenpi.autotest.webui.ui.Button;

/**
 * 反射工具类
 * @author suren
 * @date 2016年8月24日 下午3:05:12
 */
public class BeanUtil
{
	private BeanUtil(){}
	
	/**
	 * 根据属性名称设置值
	 * @param instance
	 * @param name
	 * @param value
	 */
	public static void set(Object instance, String name, Object value)
	{
		if(value == null)
		{
			return;
		}
		
		Class<? extends Object> cls = instance.getClass();
		
		try
		{
			Method setterMethod = cls.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), value.getClass());

			if(value instanceof Button)
			{
				setterMethod.invoke(instance, new Button());
			}
			else
			{
				setterMethod.invoke(instance, value);
			}
		}
		catch(NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
}
