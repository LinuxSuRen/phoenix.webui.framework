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

package org.suren.autotest.web.framework.core.ui;

import java.io.File;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.FileUploadAble;
import org.suren.autotest.web.framework.core.action.RandomFileUploadAble;

/**
 * 文件上传按钮
 * 
 * @author suren
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT 2016年7月19日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileUpload extends AbstractElement
{

	@Autowired
	private FileUploadAble	fileUploadAble;
	@Autowired
	private RandomFileUploadAble randomFileUploadAble;
	
	/** 待上传文件 */
	private File targetFile;
	
	public boolean click()
	{
		fileUploadAble.click(this);
		return true;
	}

	/**
	 * 上传指定的文件
	 * @param file
	 * @return
	 */
	public boolean upload(File file)
	{
		return fileUploadAble.upload(this, file);
	}
	
	/**
	 * 从网络中上传指定的文件
	 * @param url
	 * @return
	 */
	public boolean upload(URL url)
	{
		return fileUploadAble.upload(this, url);
	}
	
	/**
	 * 上传默认的文件
	 * @return
	 */
	public boolean upload()
	{
		return upload(targetFile);
	}
	
	/**
	 * @return 随机上传文件结果
	 */
	public boolean randomUpload()
	{
		return randomFileUploadAble.upload(this);
	}

	@Override
	public boolean isEnabled()
	{
		return fileUploadAble.isEnabled(this);
	}

	@Override
	public boolean isHidden()
	{
		return fileUploadAble.isHidden(this);
	}

	/**
	 * @return the targetFile
	 */
	public File getTargetFile()
	{
		return targetFile;
	}

	/**
	 * @param targetFile the targetFile to set
	 */
	public void setTargetFile(File targetFile)
	{
		this.targetFile = targetFile;
	}

}
