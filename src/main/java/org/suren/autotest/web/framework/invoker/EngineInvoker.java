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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.settings.Phoenix;

/**
 * 引擎相关的操作
 * @author <a href="http://surenpi.com">suren</a>
 */
public class EngineInvoker
{
	/**
	 * 关闭当前窗口
	 * @param phoenix 引擎
	 */
	public static void closeWin(Phoenix phoenix)
	{
		phoenix.getEngine().getDriver().close();
	}
	
	/**
	 * 关闭url以指定字符串开头的window
	 * @param phoenix 引擎
	 * @param params 参数
	 */
	public static void closeWinByUrlStartWith(Phoenix phoenix, String[] params)
	{
		String startWith = params[0];
		WebDriver driver = phoenix.getEngine().getDriver();
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> handleIt = handles.iterator();
		
		String currentHandle = driver.getWindowHandle();
		while(handleIt.hasNext())
		{
			String handle = handleIt.next();
			
			driver.switchTo().window(handle);
			
			if(driver.getCurrentUrl().startsWith(startWith))
			{
				driver.close();
				break;
			}
		}
		
		driver.switchTo().window(currentHandle);
	}
	
	/**
	 * 根据index来切换iframe
	 * @param phoenix 引擎
	 * @param params 参数
	 */
	public static void frameSwitchByIndex(Phoenix phoenix, String[] params)
	{
		String indexStr = params[0];
		int index = Integer.parseInt(indexStr);
		phoenix.getEngine().getDriver().switchTo().frame(index);
	}
	
	/**
	 * 根据name或者id来切换iframe
	 * @param phoenix 引擎
	 * @param params 参数
	 */
	public static void frameSwitchByNameOrId(Phoenix phoenix, String[] params)
	{
		String nameOrId = params[0];
		phoenix.getEngine().getDriver().switchTo().frame(nameOrId);
	}
	
	/**
	 * window窗口切换
	 * @param phoenix 引擎
	 */
	public static void windowSwitch(Phoenix phoenix)
	{
		SeleniumEngine engine = phoenix.getEngine();
		WebDriver driver = engine.getDriver();
		Set<String> handlers = driver.getWindowHandles();
		Iterator<String> it = handlers.iterator();
		while(it.hasNext())
		{
			String name = it.next();
			
			driver.switchTo().window(name);
		}
	}
	
	/**
	 * 截屏并保存到指定文件中
	 * @param phoenix 引擎
	 * @param params 保存截图的文件路径
	 */
	public static void takeShot(Phoenix phoenix, String[] params)
	{
		SeleniumEngine engine = phoenix.getEngine();
		WebDriver driver = engine.getDriver();
		TakesScreenshot shot = (TakesScreenshot) driver;
		
		File tmpPicFile = shot.getScreenshotAs(OutputType.FILE);
		
		String targetFile = params[0];
		try(InputStream input = new FileInputStream(tmpPicFile);
				FileOutputStream output = new FileOutputStream(new File(targetFile)))
		{
			IOUtils.copy(input, output);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
