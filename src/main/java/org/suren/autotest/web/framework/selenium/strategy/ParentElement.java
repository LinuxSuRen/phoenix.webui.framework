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

package org.suren.autotest.web.framework.selenium.strategy;

import org.openqa.selenium.WebElement;

/**
 * 设置父元素的接口
 * @author suren
 * @date 2016年12月30日 下午7:48:26
 */
public interface ParentElement
{
	/**
	 * 设置父元素
	 * @param parentWebElement
	 */
	void setParent(WebElement parentWebElement);
}
