/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.report.record;


/**
 * 动作信息记录
 * @author suren
 * @date 2016年9月6日 下午8:28:32
 */
public class ActionRecord extends ExceptionRecord
{
	/** 动作名称 */
	private String name;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
