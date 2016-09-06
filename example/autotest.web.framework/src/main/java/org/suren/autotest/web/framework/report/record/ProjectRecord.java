/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.report.record;

import java.util.List;

/**
 * 项目信息记录
 * @author suren
 * @date 2016年9月6日 下午8:31:35
 */
public class ProjectRecord
{
	private List<SuiteRecord> suiteRecordList;

	/**
	 * @return the suiteRecordList
	 */
	public List<SuiteRecord> getSuiteRecordList()
	{
		return suiteRecordList;
	}

	/**
	 * @param suiteRecordList the suiteRecordList to set
	 */
	public void setSuiteRecordList(List<SuiteRecord> suiteRecordList)
	{
		this.suiteRecordList = suiteRecordList;
	}
}
