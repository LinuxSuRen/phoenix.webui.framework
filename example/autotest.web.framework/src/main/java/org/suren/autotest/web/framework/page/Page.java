/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import java.awt.AWTException;
import java.awt.Robot;

import org.openqa.selenium.WebDriver;
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
	public final void open()
	{
		engine.openUrl(url);
		engine.computeToolbarHeight();
	}

	/**
	 * 关闭当前页面
	 */
	public final void close()
	{
		engine.close();
	}
	
	/**
	 * 关闭当前窗口以外的所有窗口并切换到当前窗口
	 */
	public final void closeOthers()
	{
		String currentTitle = getTitle();
		String currentWinHandle = engine.getDriver().getWindowHandle();
		
		for(String winHandle : engine.getDriver().getWindowHandles())
		{
			WebDriver itemDrive = engine.getDriver().switchTo().window(winHandle);
			if(!itemDrive.getTitle().equals(currentTitle))
			{
				itemDrive.close();
			}
		}
		
		engine.getDriver().switchTo().window(currentWinHandle);
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
	public final String getCurrentUrl()
	{
		return engine.getDriver().getCurrentUrl();
	}
	
	/**
	 * @return 当前页面源码
	 */
	public final String getPageSource()
	{
		return engine.getDriver().getPageSource();
	}

	/**
	 * @return 当前页面的title
	 */
	public final String getTitle()
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
	
	public void mouseWheel()
	{
		mouseWheel(1);
	}
	
	public void mouseWheel(int num)
	{
		try
		{
			new Robot().mouseWheel(num);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}
}
