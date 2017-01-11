/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2017年1月11日 下午1:32:02
 */
@Component
public class CallbackDynamicData implements DynamicData
{

	@Override
	public String getValue(String orginData)
	{
		return orginData;
	}

	@Override
	public String getType()
	{
		return "callback";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

}
