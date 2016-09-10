/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.suite;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.dom4j.DocumentException;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.settings.SettingUtil;
import org.suren.autotest.web.framework.settings.SuiteParser;

/**
 * 测试套件运行入口类
 * @author suren
 * @date 2016年9月7日 下午10:31:09
 */
public class SuiteRunner
{
	public static void main(String[] args)
	{
		if(args == null)
		{
			System.out.println("need runner_suite.xml, please recheck!");
			return;
		}
		else
		{
			System.out.println(Arrays.toString(args));
		}
		
		SuiteParser suiteParser = new SuiteParser();
		for(String path : args)
		{
			try
			{
				runFromClasspathFile(suiteParser, path);
			}
			catch (NoSuchFieldException e)
			{
				e.printStackTrace();
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (DocumentException e)
			{
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 从类路径中查找配置文件
	 * @param suiteParser
	 * @param filePath
	 * @throws IOException
	 * @throws DocumentException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InterruptedException
	 */
	private static void runFromClasspathFile(SuiteParser suiteParser, String filePath)
			throws IOException, DocumentException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException
	{
		ClassLoader classLoader = SuiteRunner.class.getClassLoader();
		
		Enumeration<URL> resources = classLoader.getResources(filePath);
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
					
					Thread.sleep(action.getBeforeSleep());
					
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
					
					Thread.sleep(action.getAfterSleep());
				}
			}
			
			Thread.sleep(suite.getAfterSleep());
		}
	}
}
