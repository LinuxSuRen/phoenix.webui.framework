/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.stereotype.Component;

/**
 * JavaScript版本的动态数据实现
 * @author suren
 * @date 2017年1月11日 下午12:49:42
 */
@Component
public class JavaScriptDynamicData implements DynamicData
{

	@Override
	public String getValue(String orginData)
	{
		String value = null;
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
		try
		{
			Object resObj = engine.eval(orginData);
			if(resObj != null)
			{
				value = resObj.toString();
			}
			else
			{
				value = "js not return!";
			}
		}
		catch (ScriptException e)
		{
			value = e.getMessage();
			e.printStackTrace();
		}
		
		return value;
	}

	@Override
	public String getType()
	{
		return "javascript";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

}
