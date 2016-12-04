/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

/**
 * 操作鼠标的接口
 * @author suren
 * @date 2016年11月25日 上午8:05:50
 */
public interface Mouse
{
	/**
	 * 向下滚动一次
	 */
	void wheel();
	/**
	 * 向下滚动n次
	 * @param num
	 */
	void wheel(int num);
	/**
	 * 左单击
	 */
	void click();
	/**
	 * 右单击
	 */
	void rightClick();
}
