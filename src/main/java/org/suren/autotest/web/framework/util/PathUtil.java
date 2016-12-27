/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.util;

import java.io.File;

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
}
