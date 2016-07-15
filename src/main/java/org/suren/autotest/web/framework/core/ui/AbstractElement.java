/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.ui;

/**
 * 所有HTML页面元素的抽象，包含了元素的id、name、css、xpath、linktext等属性
 * @author zhaoxj
 * @since jdk1.6
 * 2016年6月30日
 */
public class AbstractElement implements Element{
	private String id;
	private String name;
	private String CSS;
	private String XPath;
	private String linkText;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCSS() {
		return CSS;
	}
	public void setCSS(String cSS) {
		CSS = cSS;
	}
	public String getXPath() {
		return XPath;
	}
	public void setXPath(String xPath) {
		XPath = xPath;
	}
	public String getLinkText() {
		return linkText;
	}
	public void setLinkText(String linkText) {
		this.linkText = linkText;
	}
}
