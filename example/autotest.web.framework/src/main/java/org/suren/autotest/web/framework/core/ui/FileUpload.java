/**
 * http://surenpi.com
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
