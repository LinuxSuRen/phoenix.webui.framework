/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import org.suren.autotest.web.framework.page.Page;

/**
 * 数据源顶层接口
 * @author suren
 * @date Jul 17, 2016 8:45:03 AM
 */
public interface DataSource
{
	/**
	 * 从数据资源接口中加载数据到Page类中
	 * @param resource
	 * @param page
	 * @return
	 */
	boolean loadData(DataResource resource, Page page);
}
