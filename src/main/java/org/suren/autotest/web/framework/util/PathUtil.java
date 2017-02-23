/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * 用于获取全局的文件根目录的工具类
 * @author suren
 * @date 2016年12月27日 上午8:12:18
 */
public class PathUtil
{
	/**
	 * @return 用于保存框架缓存数据的根目录，如果不存在会自动创建
	 */
	public static File getRootDir()
	{
		String rootDir = System.getProperty("user.home", ".");
		File rootFile = new File(rootDir, ".autotest");
		if(!rootFile.isDirectory())
		{
			rootFile.mkdirs();
		}
		
		return rootFile;
	}
	
	/**
	 * 如果存在，则跳过，不复制
	 * @param input
	 * @param fileName
	 * @return
	 */
	public static File copyFileToRoot(InputStream input, String fileName)
	{
		File rootFile = PathUtil.getRootDir();
		File targetFile = new File(rootFile, fileName);
		if(!targetFile.isFile())
		{
			copyFileToRoot(input, targetFile);
		}
		
		return targetFile;
	}
	
	/**
	 * 把目标文件拷贝到框架的缓存根目录中
	 * @param input 需要调用者自行调用该输入流的close方法
	 * @param targetFile
	 * @return
	 */
	public static boolean copyFileToRoot(InputStream input, File targetFile)
	{
		try(OutputStream output = new FileOutputStream(targetFile))
		{
			IOUtils.copy(input, output);
			
			return true;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
}
