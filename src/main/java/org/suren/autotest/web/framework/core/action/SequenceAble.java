/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.action;

import java.util.List;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 序列操作
 * @author suren
 * @date 2017年1月11日 下午4:53:00
 */
public interface SequenceAble
{
	/**
	 * 执行序列操作
	 * @param element 触发该操作的元素
	 * @param actions 序列操作信息
	 */
	void perform(Element element, List<String> actions);
	
	/**
	 * @return 当前序列操作的名称
	 */
	String getName();
	
	/**
	 * @return 描述信息
	 */
	String getDescription();
}
