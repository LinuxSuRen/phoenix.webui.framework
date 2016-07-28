/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * 对HTML页面的逻辑封装，不一定是一一对应
 * @author suren
 * @date Jul 17, 2016 9:06:52 AM
 */
public class Page
{
	/** 页面唯一标示 */
	private String			id;
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

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return 给当前对象设置的url地址
	 */
	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
	
	/**
	 * @return 获取当前页面的url地址
	 */
	public String getCurrentUrl()
	{
		return engine.getDriver().getCurrentUrl();
	}
	
	/**
	 * @return 当前页面源码
	 */
	public String getPageSource()
	{
		return engine.getDriver().getPageSource();
	}

	/**
	 * @return 当前页面的title
	 */
	public String getTitle()
	{
		return engine.getDriver().getTitle();
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
