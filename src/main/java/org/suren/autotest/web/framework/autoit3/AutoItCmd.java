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

package org.suren.autotest.web.framework.autoit3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suren.autotest.webdriver.downloader.PathUtil;

import com.surenpi.autotest.utils.StringUtils;

/**
 * 使用autoid来实现文件上传。
 * @author suren
 * @since 2016年7月19日 上午8:13:28
 */
public class AutoItCmd
{

	private static final Logger logger = LoggerFactory.getLogger(AutoItCmd.class);
	
	public static String autoitExe = null;
	public static final String AUTO_IT3_PATH = "autoit3.properties";
	public static final String FILE_CHOOSE_SCRIPT = "file_choose.au3";
	
	public static final String PRO_PATH = "path";
	
	private static Properties autoItPro = new Properties();
	
	private static boolean isRunning = false;
	private Process process;
	
	static
	{
		try
		{
			Enumeration<URL> resources = AutoItCmd.class.getClassLoader().getResources(AUTO_IT3_PATH);
			if(resources != null)
			{
				while(resources.hasMoreElements())
				{
					URL url = resources.nextElement();
					
					try(InputStream input = url.openStream())
					{
						
						autoItPro.load(input);
						
						autoitExe = autoItPro.getProperty(PRO_PATH);
					}
				}
			}
			else
			{
				throw new RuntimeException("Can not found " + AUTO_IT3_PATH + " in class path.");
			}
		}
		catch (IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * @see #execFileChoose(String, File)
	 * @param file 文件
	 */
    public void execFileChoose(File file)
	{
		execFileChoose(null, file);
	}
	
	/**
	 * 执行文件选择
	 * @param title 标题
	 * @param file 文件
	 */
	public void execFileChoose(String title, File file)
	{
		if(StringUtils.isBlank(title))
		{
			title = autoItPro.getProperty("dialog.title");
		}
		
		if(file == null || !file.isFile())
		{
			throw new RuntimeException(String.format("File is null or not a file [%s].", file));
		}
		
		execFileChoose(title, file.getAbsolutePath());
	}
	
	/**
	 * 执行文件选择
	 * @param title 标题
	 * @param filePath 文件路径
	 */
	public void execFileChoose(String title, String filePath)
	{
		try
		{
			if(autoItNotExists())
			{
				synchronized (this)
				{
					notifyAll();
				}
				
				throw new RuntimeException(
						String.format("Can not found autoIt exe file in path %s, "
								+ "please download then install it, and set it in file %s.",
								autoitExe, AUTO_IT3_PATH));
			}
			
			String au3ExePath = getFileChooseScriptPath();
			
			filePath = new File(filePath).getAbsolutePath();
			String cmd = String.format("\"%s\" \"%s\" \"%s\" \"%s\"",
					autoitExe, au3ExePath, title, filePath);
			cmd = URLDecoder.decode(cmd, "utf-8");
			
			logger.debug(String.format("prepare to exec autoit cmd [%s]", cmd));
			
			process = Runtime.getRuntime().exec(cmd);
			
			synchronized (this)
			{
				isRunning = true;
				
				notifyAll();
			}
			
			process.waitFor();
		}
		catch (IOException | InterruptedException e)
		{
			logger.error(e.getMessage(), e);
		}
		finally
		{
			isRunning = false;
		}
	}
	
	public void close()
	{
		if(process != null)
		{
			process.destroy();
		}
	}

	/**
	 * @return 检查autoit的可执行文件是否存在
	 */
	private boolean autoItNotExists()
	{
		File autoitExeFile = new File(autoitExe);
		
		return !(autoitExeFile.isFile() && autoitExeFile.canExecute());
	}

	/**
	 * 获取autoit选择文件的脚本所在路径
	 * @return
	 */
	private String getFileChooseScriptPath()
	{
		URL fileChooseURL = AutoItCmd.class.getClassLoader().getResource(FILE_CHOOSE_SCRIPT);
		if(fileChooseURL != null)
		{
			try(InputStream fileChooseInput = fileChooseURL.openStream())
			{
				//为了方便用户修改，需要拷贝到缓存目录中
				File fileChooseScriptFile =
						PathUtil.copyFileToRoot(fileChooseInput, FILE_CHOOSE_SCRIPT);
				
				return fileChooseScriptFile.getAbsolutePath();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public boolean isRunning()
	{
		return isRunning;
	}
}
