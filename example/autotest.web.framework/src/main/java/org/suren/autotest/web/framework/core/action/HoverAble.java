/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 悬停
 * @author suren
 * @date 2016年10月14日 下午3:36:03
 */
public interface HoverAble
{
	/**
	 * 悬停1秒
	 * @param ele
	 */
	void hover(Element ele);
	
	/**
	 * 悬停指定时长
	 * @param ele
	 * @param timeout
	 */
	void hover(Element ele, long timeout);
}
