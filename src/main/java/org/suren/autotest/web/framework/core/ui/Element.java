/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
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
}
