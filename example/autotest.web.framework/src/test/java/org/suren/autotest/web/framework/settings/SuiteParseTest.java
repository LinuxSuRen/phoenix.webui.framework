/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.suren.autotest.web.framework.core.suite.Suite;
import org.suren.autotest.web.framework.core.suite.SuiteRunner;
import org.suren.autotest.web.framework.validation.Validation;
import org.xml.sax.SAXException;

/**
 * 测试套件解析类测试
 * @author suren
 * @date 2016年9月7日 下午10:24:21
 */
public class SuiteParseTest
{
	private String suite = "runner_suite.xml";
	
	@Test
	public void test() throws FileNotFoundException, DocumentException
	{
		SuiteParser suiteParser = new SuiteParser();
		InputStream runnerInput = SuiteParseTest.class.getClassLoader().getResourceAsStream("runner_suite.xml");
		Suite suite = suiteParser.parse(runnerInput);
		System.out.println(suite);
	}
	
	@Test
	public void valid() throws SAXException, IOException
	{
		InputStream runnerInput = SuiteParseTest.class.getClassLoader().getResourceAsStream("runner_suite.xml");
		Validation.validationSuite(runnerInput);
	}
	
	@Test
	public void invoker() throws NoSuchFieldException, SecurityException,
		IllegalArgumentException, IllegalAccessException, IOException,
		DocumentException, InterruptedException, SAXException
	{
		SuiteRunner.main(new String[]{suite});
	}
}
