/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.suite;

import java.util.List;

/**
 * 测试套件对象
 * @author suren
 * @date 2016年9月7日 下午9:43:32
 */
public class Suite
{
	/** page类描述文件 */
	private String xmlConfPath;
	/** Page对象列表，按照该顺序来执行任务 */
	private List<SuitePage> pageList;
	/** 测试套件运行结束后的休眠时间（毫秒） */
	private long afterSleep;

	/**
	 * @return the xmlConfPath
	 */
	public String getXmlConfPath()
	{
		return xmlConfPath;
	}

	/**
	 * @param xmlConfPath the xmlConfPath to set
	 */
	public void setXmlConfPath(String xmlConfPath)
	{
		this.xmlConfPath = xmlConfPath;
	}

	/**
	 * @return the pageList
	 */
	public List<SuitePage> getPageList()
	{
		return pageList;
	}

	/**
	 * @param pageList the pageList to set
	 */
	public void setPageList(List<SuitePage> pageList)
	{
		this.pageList = pageList;
	}

	/**
	 * @return the afterSleep
	 */
	public long getAfterSleep()
	{
		return afterSleep;
	}

	/**
	 * @param afterSleep the afterSleep to set
	 */
	public void setAfterSleep(long afterSleep)
	{
		this.afterSleep = afterSleep;
	}
}
