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
