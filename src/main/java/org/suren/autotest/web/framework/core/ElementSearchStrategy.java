package org.suren.autotest.web.framework.core;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author suren
 * @param <T> 元素选择策略
 */
public interface ElementSearchStrategy<T>
{
	/**
	 * 根据元素信息来定位元素
	 * @param element
	 * @return
	 */
	T search(Element element);
	
	/**
	 * @return 当前策略的描述信息
	 */
	String description();
}
