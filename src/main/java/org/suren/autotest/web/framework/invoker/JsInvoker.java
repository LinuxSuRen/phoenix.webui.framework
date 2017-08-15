/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.invoker;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.suren.autotest.web.framework.settings.Phoenix;

/**
 * 用于执行js方法的外部调用
 * @author <a href="http://surenpi.com">suren</a>
 */
public class JsInvoker
{
	/**
	 * 根据元素的class属性定位并触发单击事件
	 * @param util 引擎
	 * @param params class属性
	 * @see #execute(Phoenix, String[])
	 */
	public static void clickByClassName(Phoenix util, String[] params)
	{
		String className = params[0];
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents');");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementsByClassName('%s');if(ele.length == 0){alert('can not found by %s');}", className, className));
		scriptBuf.append("ele[0].dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 根据元素id定位并触发单击事件
	 * @param util 引擎
	 * @param params 参数
	 * @see #clickByClassName(Phoenix, String[])
	 */
	public static void clickById(Phoenix util, String[] params)
	{
		String id = params[0];
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents');");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementById('%s');", id));
		scriptBuf.append("ele.dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 根据元素name定位并触发单击事件
	 * @param util 引擎
	 * @param params 参数
	 * @see #clickByClassName(Phoenix, String[])
	 */
	public static void clickByName(Phoenix util, String[] params)
	{
		String name = params[0];
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents');");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementsByName('%s');", name));
		scriptBuf.append("ele[0].dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 根据元素tagName定位并触发单击事件
	 * @param util 引擎
	 * @param params 参数
	 * @see #clickByClassName(Phoenix, String[])
	 */
	public static void clickByTagName(Phoenix util, String[] params)
	{
		String tagName = params[0];
		
		StringBuffer scriptBuf = new StringBuffer("var evt = document.createEvent('MouseEvents');");
		scriptBuf.append("evt.initEvent('click',true,true);");
		scriptBuf.append(String.format("var ele = document.getElementsByTagName('%s');", tagName));
		scriptBuf.append("ele[0].dispatchEvent(evt);");
		
		execute(util, new String[]{scriptBuf.toString()});
	}
	
	/**
	 * 定位元素，并让元素显示在屏幕的最上方
	 * @param util 引擎
	 * @param params 需要两个元素，第一个是定位的方式（ById、ByName、ByClassName、ByTagName），第二个是值
	 * 		例如：new String[]{"ById", "userName"}
	 */
	public static void scrollIntoView(Phoenix util, String[] params)
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
	public static void execute(Phoenix util, String[] params)
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
