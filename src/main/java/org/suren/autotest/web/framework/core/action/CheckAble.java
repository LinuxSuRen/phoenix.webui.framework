/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 可选择的行为动作
 * @author suren
 * @date Jul 27, 2016 12:56:46 PM
 */
public interface CheckAble
{
	/**
	 * 根据文本来选择
	 * @param element
	 * @param text
	 */
	void checkByText(Element element, String text);
	
	/**
	 * 根据值来选择
	 * @param element
	 * @param value
	 */
	void checkByValue(Element element, String value);
}
