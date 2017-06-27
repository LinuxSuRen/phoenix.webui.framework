/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.core;

import java.util.List;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 元素查找策略
 * @author suren
 * @date 2016年12月30日 下午7:16:19
 */
public interface ElementsSearchStrategy<T>
{
	/**
	 * 查找所遇的元素
	 * @param element
	 * @return
	 */
	List<T> searchAll(Element element);
}
