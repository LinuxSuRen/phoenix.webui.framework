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
import org.suren.autotest.web.framework.util.PathUtil;

/**
 * 使用autoid来实现文件上传
 * @author suren
 * @date 2016年7月19日 上午8:13:28
 */
public class AutoItCmd
{

	private static final Logger logger = LoggerFactory.getLogger(AutoItCmd.class);
	
	public static String autoitExe = null;
	private static final String AUTO_IT3_PATH = "autoit3.properties";
	private static final String FILE_CHOOSE_SCRIPT = "file_choose.au3";
	
	static
	{
		try(InputStream input = AutoItCmd.class.getClassLoader().getResourceAsStream(AUTO_IT3_PATH))
		{
			if(input == null)
			{
				throw new RuntimeException("Can not found " + AUTO_IT3_PATH + " in class path.");
			}
			
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
		if(autoItNotExists())
		{
			throw new RuntimeException(
					String.format("Can not found autoIt exe file in path %s, "
							+ "please download then install it, and set it in file %s.",
							autoitExe, AUTO_IT3_PATH));
		}
		
		String au3ExePath = getFileChooseScriptPath();
		
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
	 * @return 检查autoit的可执行文件是否存在
	 */
	private static boolean autoItNotExists()
	{
		return !(new File(autoitExe).isFile());
	}

	/**
	 * 获取autoit选择文件的脚本所在路径
	 * @return
	 */
	private static String getFileChooseScriptPath()
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
}
