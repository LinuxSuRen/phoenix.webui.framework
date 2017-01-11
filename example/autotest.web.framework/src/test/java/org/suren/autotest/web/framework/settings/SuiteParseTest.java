/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.settings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.suren.autotest.web.framework.core.suite.Suite;
import org.suren.autotest.web.framework.core.suite.SuiteRunner;
import org.suren.autotest.web.framework.page.Page;
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
	
	@Test
	public void reflectInvoker() throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		List<String> suiteList = new ArrayList<String>();
		suiteList.add(suite);
		
		Class<?> runnerCls = Class.forName(SuiteRunner.class.getName());
		Method mainMethod = runnerCls.getMethod("main", String[].class);
		mainMethod.invoke(null, new Object[]{suiteList.toArray(new String[]{})});
	}
	
	@Test
	public void springBeanTest()
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"classpath*:autoTestContext.xml",
		"classpath*:applicationContext.xml"});
		
		System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
	}
}
