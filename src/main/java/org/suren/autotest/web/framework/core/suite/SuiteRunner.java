/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.suite;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.CheckBoxGroup;
import org.suren.autotest.web.framework.core.ui.FileUpload;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.settings.SettingUtil;
import org.suren.autotest.web.framework.settings.SuiteParser;
import org.suren.autotest.web.framework.validation.Validation;
import org.xml.sax.SAXException;

/**
 * 测试套件运行入口类
 * @author suren
 * @date 2016年9月7日 下午10:31:09
 */
public class SuiteRunner
{
	private static final Logger logger = LoggerFactory.getLogger(SuiteRunner.class);
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException,
		IllegalArgumentException, IllegalAccessException, IOException, DocumentException,
		InterruptedException, SAXException
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
		
		for(String path : args)
		{
			runFromClasspathFile(path);
		}
	}
	
	/**
	 * 从类路径中查找配置文件
	 * @param filePath
	 * @throws IOException
	 * @throws DocumentException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InterruptedException
	 * @throws SAXException 
	 */
	public static void runFromClasspathFile(String filePath)
			throws IOException, DocumentException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException,
			InterruptedException, SAXException
	{
		SuiteParser suiteParser = new SuiteParser();
		ClassLoader classLoader = SuiteRunner.class.getClassLoader();
		
		Enumeration<URL> resources = classLoader.getResources(filePath);
		while(resources.hasMoreElements())
		{
			URL url = resources.nextElement();
			
			try(InputStream input4Valid = url.openStream()){
				Validation.validationSuite(input4Valid);
			}

			try(InputStream input = url.openStream())
			{
				Suite suite = suiteParser.parse(input);
				
				runSuite(suite);
			}
		}
	}

	/**
	 * 测试套件运行入口
	 * @param suite
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InterruptedException 
	 * @throws SAXException 
	 */
	private static void runSuite(Suite suite)
			throws IOException, DocumentException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException, SAXException
	{
		String xmlConfPath = suite.getXmlConfPath();
		try(SettingUtil settingUtil = new SettingUtil())
		{
			String[] xmlConfArray = xmlConfPath.split(",");
			for(String xmlConf : xmlConfArray)
			{
				settingUtil.readFromClassPath(xmlConf);
			}
			
			List<SuitePage> pageList = suite.getPageList();
			
			//执行指定的数据组（按照数据序列）
			Integer[] rowsArray = suite.getRowsArray();
			for(int row : rowsArray)
			{
				runSuiteWithData(settingUtil, row, pageList);
				
				Thread.sleep(suite.getAfterSleep());
			}
		}
	}
	
	/**
	 * 使用指定数据组来运行
	 * @param settingUtil
	 * @param row
	 * @param pageList
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InterruptedException
	 */
	private static void runSuiteWithData(SettingUtil settingUtil, int row, List<SuitePage> pageList)
			throws SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException
	{
		settingUtil.initData(row);
		
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
				performActionList(page, actionList, settingUtil);
			}
		}
	}
	
	/**
	 * 执行动作集合
	 * @param page 
	 * @param actionList
	 * @param settingUtil 
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws InterruptedException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static void performActionList(Page page, List<SuiteAction> actionList, SettingUtil settingUtil)
			throws SecurityException, InterruptedException,
			IllegalArgumentException, IllegalAccessException
	{
		for(SuiteAction action : actionList)
		{
			Page targetPage = page;
			String field = action.getField();
			
			Field pageField = null;
			int otherPage = field.indexOf(".");
			if(otherPage != -1)
			{
				String pkg = page.getClass().getPackage().getName();
				String otherPageStr = String.format("%s.%s", pkg, field.substring(0, otherPage));
				Object pageObj = settingUtil.getPage(otherPageStr);
				if(pageObj == null)
				{
					logger.error(String.format("Can not found page [%s].", otherPageStr));
					continue;
				}
				else if(!(pageObj instanceof Page))
				{
					logger.error(String.format("Not the page class [%s].", otherPageStr));
					continue;
				}
				else
				{
					targetPage = (Page) pageObj;
					field = field.substring(otherPage + 1);
				}
			}
			
			try
			{
				pageField = targetPage.getClass().getDeclaredField(field);
				pageField.setAccessible(true);
			}
			catch (NoSuchFieldException e)
			{
				e.printStackTrace();
			}
			
			Thread.sleep(action.getBeforeSleep());
			
			int repeat = action.getRepeat();

			for(int i = 0; i < repeat; i++)
			{
//				settingUtil.initPageData(targetPage, 1);
				
				String actionResult = performAction(action, pageField, targetPage, settingUtil);
				logger.debug("Action result : ", actionResult);
			}
			
			Thread.sleep(action.getAfterSleep());
		}
	}

	/**
	 * 执行动作
	 * @param SuiteAction action
	 * @param pageField
	 * @param page
	 * @param settingUtil
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static String performAction(SuiteAction action, Field pageField, 
			Page page, SettingUtil settingUtil)
			throws IllegalArgumentException, IllegalAccessException
	{
		String name = action.getName();
		String invoker = action.getInvoker();
		
		String actionResult = "void";
		
		switch(name)
		{
			case "sequence":
				Button seqBut = getFieldObj(Button.class, pageField, page);
				seqBut.sequenceOperation();
				break;
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
			case "checkByText":
			case "checkByValue":
				CheckBoxGroup CheckBoxGroup =
					getFieldObj(CheckBoxGroup.class, pageField, page);
				if(CheckBoxGroup != null)
				{
					CheckBoxGroup.selectByValue();
				}
				else
				{
					logger.warn(String.format("Can not found CheckBoxGroup by [%s].", pageField));
				}
				break;
			case "select":
			case "selectByText":
				Selector selector = getFieldObj(Selector.class, pageField, page);
				if(selector != null)
				{
					actionResult = Boolean.toString(selector.selectByText());
				}
				break;
			case "selectByIndex":
				Selector indexSelector = getFieldObj(Selector.class, pageField, page);
				if(indexSelector != null)
				{
					actionResult = Boolean.toString(indexSelector.selectByIndex());
				}
				break;
			case "selectByValue":
				Selector valueSelector = getFieldObj(Selector.class, pageField, page);
				if(valueSelector != null)
				{
					actionResult = Boolean.toString(valueSelector.selectByIndex());
				}
				break;
			case "hover":
				Button hoverBut = getFieldObj(Button.class, pageField, page);
				hoverBut.hover();
				break;
			case "invoke":
				if(StringUtils.isBlank(invoker))
				{
					logger.error("The action in page [{}] is invoke, "
							+ "but can not found invoker [{}].", page.getClass(), invoker);
					break;
				}
				
				String[] invokers = invoker.split("!");
				String invokeMethod = "execute";
				if(invokers.length > 1)
				{
					invokeMethod = invokers[1];
				}
				
				String invokeCls = invokers[0];
				
				Class<?> invokeClazz = null;
				try
				{
					if(!invokeCls.contains("."))
					{
						//这种情况下，就调用框架内部的类
						Map<Object, Object> engineConfig = settingUtil.getEngine().getEngineConfig();
						String pkg = (String) engineConfig.get("invoker.package");
						
						invokeCls = (pkg + "." + invokeCls);
					}
					
					invokeClazz = Class.forName(invokeCls);
				}
				catch (ClassNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (SecurityException e)
				{
					e.printStackTrace();
				}
				
				List<String> paramList = action.getParamList();
				Method invokeM = null;
				
				try
				{
					if(invokeM == null)
					{
						if(paramList.size() > 0)
						{
							invokeM = invokeClazz.getMethod(invokeMethod, SettingUtil.class, String[].class);
							invokeM.invoke(null, settingUtil, paramList.toArray(new String[]{}));
						}
						else
						{
							invokeM = invokeClazz.getMethod(invokeMethod, SettingUtil.class);
							invokeM.invoke(null, settingUtil);
						}
					}
				}
				catch (NoSuchMethodException e)
				{
					e.printStackTrace();
				}
				catch (SecurityException e)
				{
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}
				
				try
				{
					if(invokeM == null)
					{
						if(paramList.size() > 0)
						{
							invokeM = invokeClazz.getMethod(invokeMethod, String[].class);
							invokeM.invoke(null, new Object[]{paramList.toArray(new String[]{})});
						}
						else
						{
							invokeM = invokeClazz.getMethod(invokeMethod);
							invokeM.invoke(null);
						}
					}
				}
				catch (NoSuchMethodException e)
				{
					e.printStackTrace();
				}
				catch (SecurityException e)
				{
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					e.printStackTrace();
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
	@SuppressWarnings("unchecked")
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
