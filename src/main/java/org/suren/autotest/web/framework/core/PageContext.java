/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

import java.util.Map;

import org.suren.autotest.web.framework.page.Page;

/**
 * @author suren
 * @date 2016年7月22日 下午2:41:26
 */
public class PageContext
{
	private Map<String, Page>			pageMap;
	
	public PageContext(){}
	
	public PageContext(Map<String, Page> pageMap)
	{
		this.pageMap = pageMap;
	}

	/**
	 * @return the pageMap
	 */
	public Map<String, Page> getPageMap()
	{
		return pageMap;
	}

	/**
	 * @param pageMap the pageMap to set
	 */
	public void setPageMap(Map<String, Page> pageMap)
	{
		this.pageMap = pageMap;
	}
}
