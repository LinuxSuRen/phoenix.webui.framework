/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2017年1月11日 下午8:40:18
 */
@Component
public class SequenceDynamicData implements DynamicData
{

	@Override
	public String getValue(String orginData)
	{
		return orginData;
	}

	@Override
	public String getType()
	{
		return "sequence";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

}
