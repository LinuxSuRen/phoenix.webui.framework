/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

/**
 * 页面上下文感知接口
 * @author suren
 * @date 2016年7月22日 下午5:47:23
 */
public interface PageContextAware
{
	/**
	 * 设置页面上下文对象
	 * @param pageContext
	 */
	public void setPageContext(PageContext pageContext);
}
