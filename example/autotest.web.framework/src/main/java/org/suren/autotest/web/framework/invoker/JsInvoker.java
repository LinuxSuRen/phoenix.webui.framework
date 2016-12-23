/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.invoker;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.suren.autotest.web.framework.settings.SettingUtil;

/**
 * 用于执行js方法的外部调用
 * @author suren
 * @date 2016年12月23日 上午8:29:53
 */
public class JsInvoker
{
	/**
	 * 根据元素的class属性定位并触发单击事件
	 * @param util
	 * @param params class属性
	 * @see #execute(SettingUtil, String[])
	 */
	public static void clickByClassName(SettingUtil util, String[] params)
	{
		String className = params[0];
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents')");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementsByClassName('%s');", className));
		scriptBuf.append("ele.dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 执行js代码
	 * @param util 框架默认提供的参数
	 * @param params 要执行的js代码，如果有多个的话，将会顺序执行
	 */
	public static void execute(SettingUtil util, String[] params)
	{
		WebDriver driver = util.getEngine().getDriver();
		JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
		
		if(params != null)
		{
			for(String param : params)
			{
				jsDriver.executeScript(param);
			}
		}
	}
}
