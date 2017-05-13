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

package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 用于给文本框设置值、获取值的行为接口
 * 
 * @author suren
 * @since jdk1.7 2016年6月29日
 */
public interface ValueEditor extends Status
{
	/**
	 * 获取值
	 * @return
	 */
	Object getValue(Element ele);

	/**
	 * 设置值（清空原有值）
	 * @param value
	 */
	void setValue(Element ele, Object value);
	
	/**
	 * 表单提交
	 * 
	 * @param ele
	 */
	void submit(Element ele);
}
