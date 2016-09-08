/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.suite;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;

import org.dom4j.DocumentException;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.settings.SettingUtil;
import org.suren.autotest.web.framework.settings.SuiteParser;

/**
 * @author suren
 * @date 2016年9月7日 下午10:31:09
 */
public class SuiteRunner
{
	public static void main(String[] args) throws Exception
	{
		SuiteParser suiteParser = new SuiteParser();
		
		ClassLoader classLoader = SuiteRunner.class.getClassLoader();
		
		Enumeration<URL> resources = classLoader.getResources("runner_suite.xml");
		while(resources.hasMoreElements())
		{
			URL url = resources.nextElement();

			try(InputStream input = url.openStream())
			{
				Suite suite = suiteParser.parse(input);
				
				runSuite(suite);
			}
		}
	}

	/**
	 * @param suite
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InterruptedException 
	 */
	private static void runSuite(Suite suite) throws IOException, DocumentException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException
	{
		String xmlConfPath = suite.getXmlConfPath();
		try(SettingUtil settingUtil = new SettingUtil())
		{
			settingUtil.readFromClassPath(xmlConfPath);
			settingUtil.initData();
			
			List<SuitePage> pageList = suite.getPageList();
			for(SuitePage suitePage : pageList)
			{
				String pageCls = suitePage.getPage();
				Page page = (Page) settingUtil.getPage(pageCls);
				Class<?> pageClz = page.getClass();
				
				String url = page.getUrl();
				if(url != null)
				{
					page.open();
				}
				
				List<SuiteAction> actionList = suitePage.getActionList();
				for(SuiteAction action : actionList)
				{
					String field = action.getField();
					String name = action.getName();
					
					Field pageField = pageClz.getDeclaredField(field);
					pageField.setAccessible(true);
					
					switch(name)
					{
						case "click":
							Button but = (Button) pageField.get(page);
							but.click();
							break;
						case "fillValue":
							Text text = (Text) pageField.get(page);
							text.fillValue();
							break;
					}
				}
			}
			
			Thread.sleep(suite.getAfterSleep());
		}
	}
}
