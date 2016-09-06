/**
 * http://surenpi.com
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
