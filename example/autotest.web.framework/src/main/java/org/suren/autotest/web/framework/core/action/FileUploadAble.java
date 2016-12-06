package org.suren.autotest.web.framework.core.action;

import java.io.File;
import java.net.URL;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 文件上传接口
 * @author suren
 * @since jdk1.7
 * 2016年7月19日
 */
public interface FileUploadAble extends Status
{
	/**
	 * 根据给定的URL来上传文件
	 * @param element
	 * @param url
	 * @return
	 */
	boolean upload(Element element, URL url);
	
	/**
	 * 根据给定的File对象来上传文件
	 * @param element
	 * @param file
	 * @return
	 */
	boolean upload(Element element, File file);
	
	/**
	 * 触发元素点击操作
	 * @param element
	 * @return
	 */
	boolean click(Element element);
}
