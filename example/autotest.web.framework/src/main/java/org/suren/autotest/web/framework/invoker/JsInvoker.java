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
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents');");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementsByClassName('%s');", className));
		scriptBuf.append("ele.dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 根据元素id定位并触发单击事件
	 * @param util
	 * @param params
	 * @see #clickByClassName(SettingUtil, String[])
	 */
	public static void clickById(SettingUtil util, String[] params)
	{
		String id = params[0];
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents');");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementsById('%s');", id));
		scriptBuf.append("ele.dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 根据元素name定位并触发单击事件
	 * @param util
	 * @param params
	 * @see #clickByClassName(SettingUtil, String[])
	 */
	public static void clickByName(SettingUtil util, String[] params)
	{
		String name = params[0];
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents');");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementsByName('%s');", name));
		scriptBuf.append("ele.dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 根据元素tagName定位并触发单击事件
	 * @param util
	 * @param params
	 * @see #clickByClassName(SettingUtil, String[])
	 */
	public static void clickByTagName(SettingUtil util, String[] params)
	{
		String tagName = params[0];
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents');");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementsByTagName('%s');", tagName));
		scriptBuf.append("ele.dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 定位元素，并让元素显示在屏幕的最上方
	 * @param util
	 * @param params 需要两个元素，第一个是定位的方式（ById、ByName、ByClassName、ByTagName），第二个是值</br>
	 * 		例如：new String[]{"ById", "userName"}
	 */
	public static void scrollIntoView(SettingUtil util, String[] params)
	{
		if(params == null || params.length < 2)
		{
			return;
		}
		
		String by = params[0];
		String byVal = params[1];
		
		execute(util, new String[]{String.format("document.getElements%s('%s').scrollIntoView(true);", by, byVal)});
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
