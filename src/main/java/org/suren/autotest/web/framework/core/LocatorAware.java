/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

/**
 * @author suren
 * @date 2016年7月25日 上午8:15:20
 */
public interface LocatorAware
{
	/**
	 * 设置元素定位器的值
	 * @param value
	 */
	void setValue(String value);
	
	/**
	 * 设置超时时间
	 * @param timeout
	 */
	void setTimeout(long timeout);
}
