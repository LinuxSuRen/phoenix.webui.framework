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

package org.suren.autotest.web.framework.core;

import java.util.Map;

import org.suren.autotest.web.framework.page.Page;

/**
 * 页面上下文对象
 * @author suren
 * @date 2016年7月22日 下午2:41:26
 */
public class PageContext
{
	private Map<String, Page>			pageMap;
	
	public PageContext(){}
	
	/**
	 * @param pageMap
	 */
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
