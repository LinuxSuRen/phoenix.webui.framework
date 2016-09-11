/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.suite;

import java.util.List;

/**
 * 测试套件中的Page以及对应的动作信息
 * @author suren
 * @date 2016年9月7日 下午9:48:38
 */
public class SuitePage
{
	/** 对应的Page全路径 */
	private String pageCls;
	/** 要执行的动作集合 */
	private List<SuiteAction> actionList;
	/** 重复执行次数 */
	private int repeat;
	
	public SuitePage(String page)
	{
		this.setPage(page);
	}

	/**
	 * @return the page
	 */
	public String getPage()
	{
		return pageCls;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(String page)
	{
		this.pageCls = page;
	}

	/**
	 * @return the actionList
	 */
	public List<SuiteAction> getActionList()
	{
		return actionList;
	}

	/**
	 * @param actionList the actionList to set
	 */
	public void setActionList(List<SuiteAction> actionList)
	{
		this.actionList = actionList;
	}

	/**
	 * @return the repeat
	 */
	public int getRepeat()
	{
		return repeat;
	}

	/**
	 * @param repeat the repeat to set
	 */
	public void setRepeat(int repeat)
	{
		this.repeat = repeat;
	}
}
