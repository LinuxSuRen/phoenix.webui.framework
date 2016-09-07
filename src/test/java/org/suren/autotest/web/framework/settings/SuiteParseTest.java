/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.suren.autotest.web.framework.core.suite.Suite;

/**
 * 测试套件解析类测试
 * @author suren
 * @date 2016年9月7日 下午10:24:21
 */
public class SuiteParseTest
{
	@Test
	public void test() throws FileNotFoundException, DocumentException
	{
		SuiteParser suiteParser = new SuiteParser();
		Suite suite = suiteParser.parse(new FileInputStream(new File("D:/Program Files (x86)/Gboat-Toolkit-Suit/workspace_surenpi/autotest.web.framework/src/test/resources/runner_suite.xml")));
		System.out.println(suite);
	}
}
