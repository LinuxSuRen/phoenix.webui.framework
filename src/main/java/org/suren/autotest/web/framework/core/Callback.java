/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

/**
 * 用于回调的接口
 * @author suren
 * @date 2017年1月25日 上午11:38:41
 */
public interface Callback<T>
{
	void callback(T data);
}
