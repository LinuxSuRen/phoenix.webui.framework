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

package org.suren.autotest.web.framework.report.record;

import java.util.List;

/**
 * 套件（模块）信息记录
 * @author suren
 * @date 2016年9月6日 下午8:30:24
 */
public class SuiteRecord
{
	private List<ActionRecord> actionRecordList;
	private List<SearchRecord> searchRecordList;
	/**
	 * @return the actionRecordList
	 */
	public List<ActionRecord> getActionRecordList()
	{
		return actionRecordList;
	}
	/**
	 * @param actionRecordList the actionRecordList to set
	 */
	public void setActionRecordList(List<ActionRecord> actionRecordList)
	{
		this.actionRecordList = actionRecordList;
	}
	/**
	 * @return the searchRecordList
	 */
	public List<SearchRecord> getSearchRecordList()
	{
		return searchRecordList;
	}
	/**
	 * @param searchRecordList the searchRecordList to set
	 */
	public void setSearchRecordList(List<SearchRecord> searchRecordList)
	{
		this.searchRecordList = searchRecordList;
	}
}
