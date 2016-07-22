/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jmx;

import java.util.List;
import java.util.Map;

import org.suren.autotest.web.framework.page.Page;

/**
 * Page信息管理
 * @author suren
 * @date 2016年7月21日 下午6:48:53
 */
public interface IPageMXBean
{
	int getTotalCount();
	
	List<Page> getPageList();
}
