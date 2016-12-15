/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

import org.junit.Test;

/**
 * @author suren
 * @date 2016年12月15日 上午8:08:03
 */
public class DefaultXmlSuiteRunnerGeneratorTest
{
	@Test
	public void test()
	{
		Generator generator = new DefaultXmlSuiteRunnerGenerator();
		generator.generate("ad.xml", "src/test/resources");
	}
}
