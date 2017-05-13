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

package org.suren.autotest.web.framework.core;

/**
 * 感知定位符的接口
 * @author suren
 * @date 2016年7月25日 上午8:15:20
 */
public interface LocatorAware
{
	/**
	 * 设置元素定位器的值
	 * @param value
	 */
	void setValue(String value);
	
	/**
	 * 设置超时时间
	 * @param timeout
	 */
	void setTimeout(long timeout);
	
	/**
	 * 扩展字段
	 * @param extend
	 */
	void setExtend(String extend);
}
