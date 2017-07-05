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

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.util.EncryptorUtil;

import com.surenpi.autotest.datasource.DynamicData;

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
