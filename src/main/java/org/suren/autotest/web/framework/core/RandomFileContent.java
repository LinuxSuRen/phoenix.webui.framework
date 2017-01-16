/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

import java.io.InputStream;

/**
 * 随机文件的内容
 * @author suren
 * @date 2017年1月15日 上午10:07:53
 */
public interface RandomFileContent
{
	/**
	 * @return 随机文件的内容输入流
	 */
	InputStream getContent();
}
