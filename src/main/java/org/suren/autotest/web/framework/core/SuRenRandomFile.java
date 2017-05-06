/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.util.StringUtils;
import org.suren.autotest.webdriver.downloader.PathUtil;

/**
 * 默认提供的随机文件生成类
 * @author suren
 * @date 2017年1月15日 上午10:06:31
 */
@Component
public class SuRenRandomFile implements RandomFile
{
	@Autowired
	private SeleniumEngine			engine;
	@Autowired
	private RandomFileContent randomContent;

	@Override
	public File createFile()
	{
		String fileName = (String) engine.getEngineConfig().get("upload.file.random.name");
		if(StringUtils.isBlank(fileName))
		{
			fileName = "suren.png";
		}
		
		File randomFile = null;
		try(InputStream content = randomContent.getContent())
		{
			randomFile = PathUtil.copyFileToRoot(content, fileName);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return randomFile;
	}

}
