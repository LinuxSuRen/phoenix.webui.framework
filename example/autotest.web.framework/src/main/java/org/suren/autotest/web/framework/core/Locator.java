/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

/**
 * 页面元素定位
 * @author suren
 * @date Jul 24, 2016 5:51:19 PM
 */
public interface Locator
{
	/** 定位类型
	 * @return
	 */
	String getType();
	/**
	 * 定位信息
	 * @return
	 */
	String getValue();
}
