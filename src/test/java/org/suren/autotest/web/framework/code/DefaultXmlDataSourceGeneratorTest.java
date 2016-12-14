/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

import org.junit.Test;

/**
 * @author suren
 * @date 2016年12月14日 下午3:45:17
 */
public class DefaultXmlDataSourceGeneratorTest
{
	@Test
	public void test()
	{
		Generator generator = new DefaultXmlDataSourceGenerator();
		generator.generate("ad.xml", "src/test/resources");
	}
}
