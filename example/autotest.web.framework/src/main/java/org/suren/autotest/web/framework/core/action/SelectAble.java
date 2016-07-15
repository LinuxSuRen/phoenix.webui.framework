/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 可选择的接口行为
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT
 * 2016年7月1日
 */
public interface SelectAble {
	/**
	 * 根据文本选择
	 * @param ele TODO
	 * @param text
	 * @return
	 */
	boolean selectByText(Element ele, String text);
	
	/**
	 * 根据值选择
	 * @param value
	 * @param ele TODO
	 * @return
	 */
	boolean selectByValue(Element ele, String value);
	
	/**
	 * 根据序号选择
	 * @param ele TODO
	 * @param index
	 * @return
	 */
	boolean selectByIndex(Element ele, int index);
}
