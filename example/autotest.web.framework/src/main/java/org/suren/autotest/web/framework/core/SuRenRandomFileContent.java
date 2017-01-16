/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.stereotype.Component;

/**
 * 随机文件内容生成类
 * @author suren
 * @date 2017年1月15日 上午10:08:22
 */
@Component
public class SuRenRandomFileContent implements RandomFileContent
{

	@Override
	public InputStream getContent()
	{
		return new ByteArrayInputStream("AutoTest random file.".getBytes());
	}

}
