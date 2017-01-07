/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

/**
 * 用于描述Page中的字段
 * @author suren
 * @date 2016年12月3日 下午9:09:53
 */
public class AutoField
{
	private String name;
	private String type;
	private String comment;
	private String getterMethod;
	private String setterMethod;
	public AutoField(String name, String type)
	{
		this.name = name;
		this.type = type;
	}
	public AutoField(String name, String type, String comment)
	{
		this.name = name;
		this.type = type;
		this.comment = comment;
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
	 * @return the type
	 */
	public String getType()
	{
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	/**
	 * @return the getterMethod
	 */
	public String getGetterMethod()
	{
		return getterMethod;
	}
	/**
	 * @param getterMethod the getterMethod to set
	 */
	public void setGetterMethod(String getterMethod)
	{
		this.getterMethod = getterMethod;
	}
	/**
	 * @return the setterMethod
	 */
	public String getSetterMethod()
	{
		return setterMethod;
	}
	/**
	 * @param setterMethod the setterMethod to set
	 */
	public void setSetterMethod(String setterMethod)
	{
		this.setterMethod = setterMethod;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
}
