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

package org.suren.autotest.web.framework.core.ui;

import java.util.List;

import org.suren.autotest.web.framework.core.Locator;

/**
 * 页面元素的顶层接口
 * @author suren
 * @since jdk1.6 2016年6月30日
 */
public interface Element
{
	/**
	 * @return 标签id属性
	 */
	String getId();

	/**
	 * @return 标签name属性
	 */
	String getName();

	/**
	 * @return 标签名称
	 */
	String getTagName();

	/**
	 * @return 元素的class值
	 */
	String getCSS();

	/**
	 * @return 标签xpath路径
	 */
	String getXPath();

	/**
	 * @return 超链接文本
	 */
	String getLinkText();

	/**
	 * @return 超链接部分文本
	 */
	String getPartialLinkText();
	/**
	 * @return 定位信息map
	 */
	List<Locator> getLocatorList();

	/**
	 * @return 元素查找策略
	 */
	String getStrategy();
	
	/**
	 * @return 元素查找超时时间
	 */
	long getTimeOut();
}
