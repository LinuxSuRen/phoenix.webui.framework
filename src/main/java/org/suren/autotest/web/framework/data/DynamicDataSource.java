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

/**
 * @author suren
 * @date 2017年3月24日 下午10:20:06
 */
public interface DynamicDataSource
{
	
	/**
	 * 动态参数
	 * @param globalMap
	 */
	void setGlobalMap(Map<String, Object> globalMap);
	
	/**
	 * 动态参数
	 * @return
	 */
	Map<String, Object> getGlobalMap();
	
	/**
	 * @return 唯一标识数据源的名称
	 */
	String getName();
}
