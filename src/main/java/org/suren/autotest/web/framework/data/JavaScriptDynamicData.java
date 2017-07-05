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

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.stereotype.Component;

import com.surenpi.autotest.datasource.DynamicData;

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
