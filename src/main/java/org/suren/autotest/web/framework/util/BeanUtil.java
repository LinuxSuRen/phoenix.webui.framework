/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;


/**
 * 
 * 此处填写类简介
 * <p>
 * 此处填写类说明
 * </p>
 * @author sunyp
 * @since jdk1.6
 * 2016年6月24日
 *  
 */

public class BeanUtil {
	public static void set(Object instance, String name, Object value) {
		if(value == null)
		{
			return;
		}
		
		Class<? extends Object> cls = instance.getClass();
		
		try {
			Class<?> clz = Text.class;
			if(value instanceof Button) {
				clz = Button.class;
			}
			
			Method setterMethod = cls.getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), value.getClass());

			if(value instanceof Button) {
				setterMethod.invoke(instance, new Button());
			}else{
				setterMethod.invoke(instance, value);
			}
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
