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
 * 悬停
 * @author suren
 * @date 2016年10月14日 下午3:36:03
 */
public interface HoverAble
{
	/**
	 * 悬停1秒
	 * @param ele
	 */
	void hover(Element ele);
	
	/**
	 * 悬停指定时长
	 * @param ele
	 * @param timeout
	 */
	void hover(Element ele, long timeout);
}
