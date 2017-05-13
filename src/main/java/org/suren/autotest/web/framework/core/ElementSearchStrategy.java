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

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author suren
 * @param <T> 元素选择策略
 */
public interface ElementSearchStrategy<T>
{
	/**
	 * 根据元素信息来定位元素
	 * @param element
	 * @return
	 */
	T search(Element element);
	
	/**
	 * @return 当前策略的描述信息
	 */
	String description();
}
