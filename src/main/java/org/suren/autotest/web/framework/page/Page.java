/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * @author sunyp
 * @since jdk1.6
 * 2016年6月24日
 */
public class Page {
	/** 当前页面的url地址 */
	private String url;

	@Autowired
	private SeleniumEngine engine;
	
	/**
	 * 打开（进入）当前页面
	 */
	public void open() {
		engine.openUrl(url);
	}
	
	/**
	 * 关闭当前页面
	 */
	public void close() {
		engine.close();
	}
	
	/**
	 * 获取当前页面的url地址
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
