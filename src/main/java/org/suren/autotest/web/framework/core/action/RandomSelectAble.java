/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 随机选择
 * @author suren
 * @date 2017年1月13日 下午12:19:05
 */
public interface RandomSelectAble
{
	/**
	 * 随机选择
	 * @param ele
	 * @return 操作成功返回true，否则返回false
	 */
	boolean randomSelect(Element ele);
}
