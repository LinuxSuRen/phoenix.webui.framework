package org.suren.autotest.web.framework.core;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author suren
 * @param <T>
 * 元素选择策略
 */
public interface ElementSearchStrategy<T> {
	T search(Element element);
}
