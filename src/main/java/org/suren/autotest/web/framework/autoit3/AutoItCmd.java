/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.autoit3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用autoid来实现文件上传
 * @author suren
 * @date 2016年7月19日 上午8:13:28
 */
public class AutoItCmd
{

	private static final Logger logger = LoggerFactory.getLogger(AutoItCmd.class);
	
	public static String autoitExe = null;
	
	static
	{
		try(InputStream input = AutoItCmd.class.getClassLoader().getResourceAsStream("autoit3.properties"))
		{
			Properties pro = new Properties();
			pro.load(input);
			
			autoitExe = pro.getProperty("path");
		}
		catch (IOException e)
		{
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 执行文件选择
	 * @param title
	 * @param file
	 */
	public static void execFileChoose(String title, File file)
	{
		execFileChoose(title, file.getAbsolutePath());
	}
	
	/**
	 * 执行文件选择
	 * @param title
	 * @param filePath
	 */
	public static void execFileChoose(String title, String filePath)
	{
		String au3ExePath = getAu3ExePath();
		
		try
		{
			filePath = new File(filePath).getAbsolutePath();
			String cmd = String.format("%s \"%s\" \"%s\" \"%s\"",
					autoitExe, au3ExePath, title, filePath);
			cmd = URLDecoder.decode(cmd, "utf-8");
			
			logger.debug(String.format("prepare to exec autoit cmd [%s]", cmd));
			
			Process process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		}
		catch (IOException | InterruptedException e)
		{
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取autoit的安装目录
	 * @return
	 */
	private static String getAu3ExePath()
	{
		URL fileChooseURL = AutoItCmd.class.getClassLoader().getResource("file_choose.au3");
		if(fileChooseURL != null)
		{
			return new File(fileChooseURL.getFile()).getAbsolutePath();
		}
		else
		{
			return null;
		}
	}
}
