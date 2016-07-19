/**
 * Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
 */
package org.suren.autotest.web.framework.core.ui;

import java.io.File;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.FileUploadAble;

/**
 * 文件上传
 * 
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT 2016年7月19日
 */
@Component
public class FileUpload extends AbstractElement
{

	@Autowired
	private FileUploadAble	fileUploadAble;
	
	public boolean click()
	{
		fileUploadAble.click(this);
		return true;
	}

	public boolean upload(File file)
	{
		return fileUploadAble.upload(this, file);
	}
	
	public boolean upload(URL url)
	{
		return fileUploadAble.upload(this, url);
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

}
