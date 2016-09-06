/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 元素状态接口
 * @author suren
 * @date Jul 16, 2016 7:58:48 PM
 */
public interface Status
{
	/**
	 * @param element
	 * @return 是否可用
	 */
	boolean isEnabled(Element element);
	
	/**
	 * @param element
	 * @return 是否隐藏
	 */
	boolean isHidden(Element element);
}
