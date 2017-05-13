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
