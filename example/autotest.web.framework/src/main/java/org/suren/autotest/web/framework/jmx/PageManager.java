/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jmx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.page.Page;

/**
 * @author suren
 * @date 2016年7月21日 下午6:50:35
 */
@Component
public class PageManager implements IPageMXBean
{

	private Map<String, Page> pageMap;
	
	@Override
	public int getTotalCount()
	{
		return pageMap.size();
	}

	@Override
	public List<Page> getPageList()
	{
		return new ArrayList<Page>(pageMap.values());
	}
	
	@Override
	public void setPageMap(Map<String, Page> pageMap)
	{
		this.pageMap = Collections.unmodifiableMap(pageMap);
	}

}
