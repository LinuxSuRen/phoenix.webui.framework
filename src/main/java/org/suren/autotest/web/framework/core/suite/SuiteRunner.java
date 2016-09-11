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
import org.suren.autotest.web.framework.core.ui.CheckBoxGroup;
import org.suren.autotest.web.framework.core.ui.FileUpload;
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
	private static void runSuite(Suite suite)
			throws IOException, DocumentException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException
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
				if(page == null)
				{
					System.err.println(String.format("the page[%s] is null.", pageCls));
					continue;
				}
				
				String url = page.getUrl();
				if(url != null)
				{
					page.open();
				}
				
				List<SuiteAction> actionList = suitePage.getActionList();
				int repeat = suitePage.getRepeat();
				for(int i = 0; i < repeat; i++)
				{
					performActionList(page, actionList);
				}
			}
			
			Thread.sleep(suite.getAfterSleep());
		}
	}
	
	/**
	 * 执行动作集合
	 * @param page 
	 * @param actionList
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws InterruptedException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static void performActionList(Page page, List<SuiteAction> actionList)
			throws NoSuchFieldException, SecurityException, InterruptedException,
			IllegalArgumentException, IllegalAccessException
	{
		Class<?> pageClz = page.getClass();
		
		for(SuiteAction action : actionList)
		{
			String field = action.getField();
			String name = action.getName();
			
			Field pageField = pageClz.getDeclaredField(field);
			pageField.setAccessible(true);
			
			Thread.sleep(action.getBeforeSleep());
			
			int repeat = action.getRepeat();

			for(int i = 0; i < repeat; i++)
			{
				String actionResult = performAction(name, pageField, page);
				System.out.println(actionResult);
			}
			
			Thread.sleep(action.getAfterSleep());
		}
	}

	/**
	 * 执行动作
	 * @param name
	 * @param pageField
	 * @param page
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static String performAction(String name, Field pageField, Page page)
			throws IllegalArgumentException, IllegalAccessException
	{
		String actionResult = "void";
		
		switch(name)
		{
			case "click":
				Button but = getFieldObj(Button.class, pageField, page);
				if(but != null)
				{
					but.click();
				}
				break;
			case "fillValue":
				Text text = getFieldObj(Text.class, pageField, page);
				if(text != null)
				{
					text.fillValue();
				}
				break;
			case "upload":
				FileUpload fileUpload = getFieldObj(FileUpload.class, pageField, page);
				if(fileUpload != null)
				{
					actionResult = Boolean.toString(fileUpload.upload());
				}
				break;
			case "enter":
				Text enterText = getFieldObj(Text.class, pageField, page);
				if(enterText != null)
				{
					enterText.performEnter();
				}
				break;
			case "selectByText":
				CheckBoxGroup CheckBoxGroup =
					getFieldObj(CheckBoxGroup.class, pageField, page);
				if(CheckBoxGroup != null)
				{
					actionResult = Boolean.toString(CheckBoxGroup.selectByText());
				}
				break;
		}
		
		return actionResult;
	}

	/**
	 * 获取对应类型组件
	 * @param type
	 * @param pageField
	 * @param instance
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static <T> T getFieldObj(Class<T> type, Field pageField, Object instance)
			throws IllegalArgumentException, IllegalAccessException
	{
		Object fieldObj = pageField.get(instance);
		if(type.isInstance(fieldObj))
		{
			return (T) fieldObj;
		}
		else
		{
			return null;
		}
	}
}
