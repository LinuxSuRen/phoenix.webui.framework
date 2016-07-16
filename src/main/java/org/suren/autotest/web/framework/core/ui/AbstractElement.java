/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.ui;

/**
 * 所有HTML页面元素的抽象， 包含了元素的id、name、tagName、css、xpath、linktext、partialLinkText等属性
 * 
 * @author suren
 * @since jdk1.6 2016年6月30日
 */
public abstract class AbstractElement implements Element
{
	private String	id;
	private String	name;
	private String	tagName;
	private String	CSS;
	private String	XPath;
	private String	linkText;
	private String	partialLinkText;
	private String	strategy;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public String getCSS()
	{
		return CSS;
	}

	public void setCSS(String cSS)
	{
		CSS = cSS;
	}

	public String getXPath()
	{
		return XPath;
	}

	public void setXPath(String xPath)
	{
		XPath = xPath;
	}

	public String getLinkText()
	{
		return linkText;
	}

	public void setLinkText(String linkText)
	{
		this.linkText = linkText;
	}

	public String getPartialLinkText()
	{
		return partialLinkText;
	}

	public void setPartialLinkText(String partialLinkText)
	{
		this.partialLinkText = partialLinkText;
	}

	public String getStrategy()
	{
		return strategy;
	}

	public void setStrategy(String strategy)
	{
		this.strategy = strategy;
	}
	
	/**
	 * @return 可用返回true，否则返回false
	 */
	public abstract boolean isEnabled();
	
	/**
	 * @return 隐藏返回true，否则返回false
	 */
	public abstract boolean isHidden();
}
