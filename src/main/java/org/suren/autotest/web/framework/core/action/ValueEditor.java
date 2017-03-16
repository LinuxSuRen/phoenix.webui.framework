package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 用于给文本框设置值、获取值的行为接口
 * 
 * @author suren
 * @since jdk1.7 2016年6月29日
 */
public interface ValueEditor extends Status
{
	/**
	 * 获取值
	 * @return
	 */
	Object getValue(Element ele);

	/**
	 * 设置值（清空原有值）
	 * @param value
	 */
	void setValue(Element ele, Object value);
	
	/**
	 * 表单提交
	 * 
	 * @param ele
	 */
	void submit(Element ele);
}
