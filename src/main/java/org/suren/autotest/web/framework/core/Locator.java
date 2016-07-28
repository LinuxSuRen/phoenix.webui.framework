/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

/**
 * 页面元素定位器
 * @author suren
 * @date Jul 24, 2016 5:51:19 PM
 */
public interface Locator
{
	/**
	 * @return 定位类型
	 */
	String getType();
	/**
	 * @return 定位信息
	 */
	String getValue();
	
	/** 
	 * @return 元素定位等待的超时时间
	 */
	long getTimeout();
}
