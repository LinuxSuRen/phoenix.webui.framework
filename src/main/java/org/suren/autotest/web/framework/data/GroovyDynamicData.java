/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.springframework.stereotype.Component;

/**
 * Groovy脚本版本的动态数据实现
 * @author suren
 * @date 2017年1月11日 下午12:47:43
 */
@Component
public class GroovyDynamicData implements DynamicData
{
	private String groovyCls;
	private Map<String, Object> globalMap;
	
	public GroovyDynamicData()
	{
		StringBuffer buf = new StringBuffer();
		try(InputStream stream = XmlDataSource.class.getClassLoader().getResource("random.groovy").openStream())
		{
			byte[] bf = new byte[1024];
			int len = -1;
			while((len = stream.read(bf)) != -1)
			{
				buf.append(new String(bf, 0, len));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		groovyCls = buf.toString();
	
	}
	
	@Override
	public String getValue(String orginData)
	{
		String value;
		
		Binding binding = new Binding();
		GroovyShell shell = new GroovyShell(binding);
		
		binding.setVariable("globalMap", globalMap);
		Object resObj = null;
		try
		{
			resObj = shell.evaluate(groovyCls + orginData);
			if(resObj != null)
			{
				value = resObj.toString();
			}
			else
		 	{
				value = "groovy not return!";
			}
		}
		catch(CompilationFailedException e)
		{
			value = e.getMessage();
			e.printStackTrace();
		}
		
		return value;
	}

	@Override
	public String getType()
	{
		return "groovy";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
		globalMap = data;
	}

}
