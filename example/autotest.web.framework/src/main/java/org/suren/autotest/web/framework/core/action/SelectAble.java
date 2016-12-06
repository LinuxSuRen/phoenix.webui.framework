package org.suren.autotest.web.framework.core.action;

import java.util.List;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 可选择的接口行为
 * 
 * @author suren
 * @since jdk1.7
 * @since 2016年7月1日
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
	
	/**
	 * 选择所有元素
	 * @param element
	 * @return
	 */
	boolean selectAll(Element element);
	
	/**
	 * 根据文本来取消选择
	 * @param element
	 * @param text
	 * @return
	 */
	boolean deselectByText(Element element, String text);
	
	/**
	 * 根据值来取消选择
	 * @param element
	 * @param value
	 * @return
	 */
	boolean deselectByValue(Element element, String value);
	
	/**
	 * 根据序号来取消选择
	 * @param element
	 * @param index
	 * @return
	 */
	boolean deselectByIndex(Element element, int index);
	
	/**
	 * 取消选择所有
	 * @param element
	 * @return
	 */
	boolean deselectAll(Element element);
	
	/**
	 * 是否有多个
	 * @param element
	 * @return
	 */
	boolean isMultiple(Element element);
	
	/**
	 * 获取所有选项
	 * @param element
	 * @return
	 */
	List<Element> getOptions(Element element);
	
	/**
	 * 获取所有已选择的元素
	 * @param element
	 * @return
	 */
	List<Element> getSelectedOptions(Element element);
}
