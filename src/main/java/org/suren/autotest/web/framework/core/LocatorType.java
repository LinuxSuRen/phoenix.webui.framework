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
 * 定位方法枚举
 * @author suren
 * @date 2017年6月7日 下午7:00:15
 */
public enum LocatorType
{
	BY_ID, BY_NAME, BY_XPATH, BY_TAGNAME, BY_CSS,
	BY_LINK_TEXT,
	BY_PARTIAL_LINK_TEXT;
}
