/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.util.EncryptorUtil;

/**
 * 密文版本的动态数据实现
 * @author suren
 * @date 2017年1月11日 下午12:50:46
 */
@Component
public class EncryptDynamicData implements DynamicData
{

	@Override
	public String getValue(String orginData)
	{
		return EncryptorUtil.decryptWithBase64(orginData);
	}

	@Override
	public String getType()
	{
		return "encrypt";
	}

	@Override
	public void setData(Map<String, Object> data)
	{
	}

}
