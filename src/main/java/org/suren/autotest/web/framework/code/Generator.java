/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

import java.io.InputStream;

import org.dom4j.DocumentException;
import org.suren.autotest.web.framework.core.Callback;
import org.xml.sax.SAXException;

/**
 * 生成器
 * @author suren
 * @date 2016年12月3日 下午8:41:01
 */
public interface Generator
{
	/**
	 * 根据源文件生成Java代码
	 * @param srcCoding 源文件路径
	 * @param outputDir 输出的目录
	 * @return
	 */
	void generate(String srcCoding, String outputDir);
	
	void generate(InputStream input, String outputDir, Callback<?> callback)
			throws DocumentException, SAXException;
}
