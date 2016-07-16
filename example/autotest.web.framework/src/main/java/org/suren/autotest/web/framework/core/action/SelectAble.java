/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.action;

import java.util.List;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 可选择的接口行为
 * 
 * @author suren
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT 2016年7月1日
 */
public interface SelectAble extends Status
{
	/**
	 * 根据文本选择
	 * 
	 * @param ele
	 * @param text
	 * @return
	 */
	boolean selectByText(Element ele, String text);

	/**
	 * 根据值选择
	 * 
	 * @param value
	 * @param ele
	 * @return
	 */
	boolean selectByValue(Element ele, String value);

	/**
	 * 根据序号选择
	 * 
	 * @param ele
	 * @param index
	 * @return
	 */
	boolean selectByIndex(Element ele, int index);
	
	boolean selectAll(Element element);
	
	boolean deselectByText(Element element, String text);
	
	boolean deselectByValue(Element element, String value);
	
	boolean deselectByIndex(Element element, int index);
	
	boolean deselectAll(Element element);
	
	boolean isMultiple(Element element);
	
	List<Element> getOptions(Element element);
	
	List<Element> getSelectedOptions(Element element);
}
