/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

/**
 * @author suren
 * @date 2016年12月3日 下午9:09:53
 */
public class AutoField
{
	private String name;
	private String type;
	private String getterMethod;
	private String setterMethod;
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
}
