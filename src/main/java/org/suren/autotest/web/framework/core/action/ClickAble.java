package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 可点击的接口行为，包括：单击、双击等
 * 
 * @author suren
 * @since jdk1.7 2016年6月29日
 */
public interface ClickAble extends Status
{
	/**
	 * 单击
	 */
	void click(Element ele);

	/**
	 * 双击
	 */
	void dbClick(Element ele);
}
