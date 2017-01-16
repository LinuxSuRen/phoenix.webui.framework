package org.suren.autotest.web.framework.core.action;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 上传随机生成文件接口
 * @author suren
 * @date 2017年1月15日 上午9:46:43
 */
public interface RandomFileUploadAble extends Status
{
	/**
	 * 随机上传一个文件
	 * @param element
	 * @return
	 */
	boolean upload(Element element);
}
