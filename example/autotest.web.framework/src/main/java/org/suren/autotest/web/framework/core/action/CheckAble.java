/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author suren
 * @date Jul 27, 2016 12:56:46 PM
 */
public interface CheckAble
{
	/**
	 * 根据文本来选择
	 * @param text
	 */
	void checkByText(Element element, String text);
}
