/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.jmx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.PageContext;
import org.suren.autotest.web.framework.core.PageContextAware;
import org.suren.autotest.web.framework.page.Page;

/**
 * @author suren
 * @date 2016年7月21日 下午6:50:35
 */
@Component
public class PageManager implements IPageMXBean, PageContextAware
{

	private PageContext pageContext;
	
	@Override
	public int getTotalCount()
	{
		return pageContext.getPageMap().size();
	}

	@Override
	public List<Page> getPageList()
	{
		return new ArrayList<Page>(pageContext.getPageMap().values());
	}
	
	@Override
	public void setPageContext(PageContext pageContext)
	{
		this.pageContext = pageContext;
	}

}
