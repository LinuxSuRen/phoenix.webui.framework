package org.suren.autotest.web.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.suren.autotest.web.framework.core.ui.Button;

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
		
		try {
			Method setterMethod = cls.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), value.getClass());

			if(value instanceof Button) {
				setterMethod.invoke(instance, new Button());
			}else{
				setterMethod.invoke(instance, value);
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
