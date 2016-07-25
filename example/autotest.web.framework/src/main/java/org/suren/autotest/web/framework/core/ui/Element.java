/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.ui;

import java.util.List;

import org.suren.autotest.web.framework.core.Locator;

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
	List<Locator> getLocatorList();

	/**
	 * @return 元素查找策略
	 */
	String getStrategy();
}
