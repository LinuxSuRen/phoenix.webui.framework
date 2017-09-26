/**
 * 
 */
package org.suren.autotest.web.framework.data;

import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.surenpi.autotest.datasource.DynamicData;

/**
 * @author surenpi
 *
 */
@Component
public class EnvDynamicData implements DynamicData
{

	@Override
	public String getType()
	{
		return "env";
	}

	@Override
	public String getValue(String value)
	{
		if(value == null)
		{
			return null;
		}
		
		Iterator<String> it = System.getenv().keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			value = value.replace("${" + key + "}", System.getenv(key));
		}
		Iterator<Object> sysIt = System.getProperties().keySet().iterator();
        while(sysIt.hasNext())
        {
            Object key = sysIt.next();
            value = value.replace("${" + key.toString() + "}", System.getProperty(key.toString()));
        }
		
		return value;
	}

	@Override
	public void setData(Map<String, Object> arg0)
	{
	}

}
