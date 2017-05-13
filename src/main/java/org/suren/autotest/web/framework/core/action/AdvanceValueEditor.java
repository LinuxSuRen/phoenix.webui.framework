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
 * 高级文本值填入接口
 * @author suren
 * @date 2017年3月16日 下午8:22:42
 */
public interface AdvanceValueEditor extends ValueEditor
{
	/**
	 * 在原有值的基础上追加，允许值为空
	 * @param ele
	 * @param value
	 */
	void appendValue(Element ele, Object value);
	
	/**
	 * 填入为空值，如果值为空会抛出异常
	 * @param ele
	 * @param value 该值为空抛出异常
	 */
	void fillNotBlankValue(Element ele, Object value);
}
