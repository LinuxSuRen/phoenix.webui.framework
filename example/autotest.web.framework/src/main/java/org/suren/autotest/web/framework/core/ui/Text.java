/**
* http://surenpi.com
*/
package org.suren.autotest.web.framework.core.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ClickAble;
import org.suren.autotest.web.framework.core.action.ValueEditor;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * 文本框封装
 * 
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Text extends AbstractElement
{
	private String		value;
	private String 		callback;

	@Autowired
	private ValueEditor	valueEditor;
	@Autowired
	private ClickAble	clickAble;
	@Autowired
	private SeleniumEngine engine;

	public Text()
	{
	}

	public Text(String value)
	{
		this.value = value;
	}

	/**
	 * 自动填充数据，不用关心数据来源
	 */
	public Text fillValue()
	{
		String val4Fill = value;
		if(StringUtils.isNotBlank(callback))
		{
			String methodName = "execute";
			String callbackClsName = callback;
			
			int methodIndex = callback.indexOf("!");
			if(methodIndex != -1 && !callback.endsWith("!"))
			{
				methodName = callback.substring(methodIndex + 1);
				callbackClsName = callback.substring(0, methodIndex);
			}
			
			try
			{
				Class<?> callbackCls = Class.forName(callbackClsName);
				Method callbackMethod = callbackCls.getMethod(methodName,
						SeleniumEngine.class, String.class);
				
				Object result = callbackMethod.invoke(null, engine, value);
				if(result != null)
				{
					val4Fill = result.toString();
				}
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch (SecurityException e)
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
		}
		
		valueEditor.setValue(this, val4Fill);
		
		return this;
	}

	/**
	 * @return 预备的数据
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * 预备数据
	 * @param value
	 */
	public Text setValue(String value)
	{
		this.value = value;
		return this;
	}
	
	/**
	 * 执行点击操作
	 */
	public Text click()
	{
		getClickAble().click(this);
		return this;
	}
	
	/**
	 * 执行回车操作
	 * @return
	 */
	public Text performEnter()
	{
		valueEditor.submit(this);
		
		return this;
	}

	@Override
	public boolean isEnabled()
	{
		return valueEditor.isEnabled(this);
	}

	@Override
	public boolean isHidden()
	{
		return valueEditor.isHidden(this);
	}

	/**
	 * @return 可填入值的行为接口
	 */
	public ValueEditor getValueEditor()
	{
		return valueEditor;
	}

	/**
	 * 设置用于填入值的行为
	 * @param valueEditor
	 */
	public void setValueEditor(ValueEditor valueEditor)
	{
		this.valueEditor = valueEditor;
	}

	/**
	 * @return 可点击的行为接口
	 */
	public ClickAble getClickAble()
	{
		return clickAble;
	}

	/**
	 * 设置可点击的行为
	 * @param clickAble
	 */
	public void setClickAble(ClickAble clickAble)
	{
		this.clickAble = clickAble;
	}

	public String getCallback()
	{
		return callback;
	}

	public void setCallback(String callback)
	{
		this.callback = callback;
	}
}
