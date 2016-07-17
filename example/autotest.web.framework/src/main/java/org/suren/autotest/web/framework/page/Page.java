/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * @author suren
 * @date Jul 17, 2016 9:06:52 AM
 */
public class Page
{
	/** 当前页面的url地址 */
	private String			url;
	/** 当前页面所关联的数据源 */
	private String			dataSource;

	@Autowired
	private SeleniumEngine	engine;

	/**
	 * 打开（进入）当前页面
	 */
	public void open()
	{
		engine.openUrl(url);
	}

	/**
	 * 关闭当前页面
	 */
	public void close()
	{
		engine.close();
	}

	/**
	 * 获取当前页面的url地址
	 * 
	 * @return
	 */
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(String dataSource)
	{
		this.dataSource = dataSource;
	}
}
