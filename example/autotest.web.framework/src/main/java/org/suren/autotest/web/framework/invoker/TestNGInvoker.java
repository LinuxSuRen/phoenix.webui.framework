/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 调用外部的TestNG脚本
 * @author suren
 * @date 2016年12月12日 下午12:30:01
 */
public class TestNGInvoker
{
	public static void execute(String[] params)
	{
		try
		{
			Class<?> testNGCls = Class.forName("org.testng.TestNG");
			Class<?> testNGListenerCls = Class.forName("org.testng.ITestListener");
			
			Method mainMethod = testNGCls.getMethod("privateMain", String[].class, testNGListenerCls);

			mainMethod.invoke(null, params, null);
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
