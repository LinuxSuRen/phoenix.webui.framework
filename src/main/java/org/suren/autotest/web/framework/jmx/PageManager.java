/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.jmx;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.surenpi.autotest.webui.Page;
import com.surenpi.autotest.webui.core.PageContext;
import com.surenpi.autotest.webui.core.PageContextAware;

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
