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

import org.springframework.stereotype.Component;

/**
 * 系统参数配置
 * @author suren
 * @date 2017年3月31日 下午9:51:54
 */
@Component
public class SystemParamDynamicData extends PropertiesDynamicData
{
	@Override
	public String getType()
	{
		return "system";
	}

	@Override
	public String getPath()
	{
		return "system.data.properties";
	}

}
