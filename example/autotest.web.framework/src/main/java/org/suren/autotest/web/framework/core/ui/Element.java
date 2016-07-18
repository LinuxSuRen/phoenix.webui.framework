/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.ui;

import java.util.LinkedHashMap;

/**
 * @author suren
 * @since jdk1.6 2016年6月30日
 */
public interface Element
{
	String getId();

	String getName();

	String getTagName();

	/**
	 * @return 元素的class值
	 */
	String getCSS();

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
	LinkedHashMap<String, String> getLocatorsMap();

	/**
	 * @return 元素查找策略
	 */
	String getStrategy();
}
