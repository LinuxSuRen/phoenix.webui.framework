/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

import java.util.List;

/**
 * 用于自动生成Page类的模型
 * @author suren
 * @date 2016年12月3日 下午9:11:42
 */
public class AutoPage
{
	/** Page类注释 */
	private String comment;
	/** Page类所在包 */
	private String packageName;
	/** Page类名 */
	private String name;
	/** Page类中的属性集合 */
	private List<AutoField> fields;
	/**
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	/**
	 * @return the packageName
	 */
	public String getPackageName()
	{
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}
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
	/**
	 * @return the fields
	 */
	public List<AutoField> getFields()
	{
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<AutoField> fields)
	{
		this.fields = fields;
	}
}
