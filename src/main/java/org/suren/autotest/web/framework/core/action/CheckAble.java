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
 * 可选择的行为动作
 * @author suren
 * @date Jul 27, 2016 12:56:46 PM
 */
public interface CheckAble
{
	/**
	 * 根据文本来选择
	 * @param element
	 * @param text
	 */
	void checkByText(Element element, String text);
	
	/**
	 * 根据值来选择
	 * @param element
	 * @param value
	 */
	void checkByValue(Element element, String value);
}
