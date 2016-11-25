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
	void wheel();
	void wheel(int num);
	void click();
	void rightClick();
}
