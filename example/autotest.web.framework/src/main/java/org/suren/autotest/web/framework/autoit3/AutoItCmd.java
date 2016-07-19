/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.autoit3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.io.IOUtils;

/**
 * @author suren
 * @date 2016年7月19日 上午8:13:28
 */
public class AutoItCmd
{
	public static String autoitExe = null;
	
	static
	{
		InputStream input = null;
		try
		{
			input = AutoItCmd.class.getClassLoader().getResourceAsStream("autoit3.properties");
			
			Properties pro = new Properties();
			pro.load(input);
			
			autoitExe = pro.getProperty("path");
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(input);
		}
	}
	
	public static void execFileChoose(String title, File file)
	{
		execFileChoose(title, file.getAbsolutePath());
	}
	
	public static void execFileChoose(String title, String filePath)
	{
		String au3ExePath = getAu3ExePath();
		
		try
		{
			Runtime.getRuntime().exec(String.format("%s %s \"%s\" \"%s\"",
					autoitExe, au3ExePath, title, filePath));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
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
	
	public static void main(String[] args)
	{
		execFileChoose("文件上传", new File("d:/a.jpg"));
	}
}
