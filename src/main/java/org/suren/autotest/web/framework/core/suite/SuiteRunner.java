/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.suite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suren.autotest.web.framework.core.ProgressInfo;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.CheckBoxGroup;
import org.suren.autotest.web.framework.core.ui.FileUpload;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.settings.SettingUtil;
import org.suren.autotest.web.framework.settings.SuiteParser;
import org.suren.autotest.web.framework.util.StringUtils;
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
		
		SuiteRunner suiteRunner = new SuiteRunner();
		for(String path : args)
		{
			suiteRunner.runFromClasspathFile(path);
		}
	}
	
	private ProgressInfo<String> progressInfo;
	
	public SuiteRunner()
	{
		setEmptyProgress();
	}
	
	/**
	 * @param progressInfo 进度信息设置接口
	 */
	public SuiteRunner(ProgressInfo<String> progressInfo)
	{
		this.progressInfo = progressInfo;
		if(progressInfo == null)
		{
			setEmptyProgress();
		}
	}
	
	/**
	 * 添加空白的进度保存实现
	 */
	private void setEmptyProgress()
	{
		this.progressInfo = new ProgressInfo<String>()
		{
			//空实现，为了能提高效率
			@Override
			public void setInfo(String data){}

			@Override
			public void setIdentify(String id){}

			@Override
			public String getIdentify()
			{
				return null;
			}

			@Override
			public void setStatus(int status){}
		};
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
	public void runFromClasspathFile(String filePath)
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
			
			try(InputStream input4Valid = url.openStream())
			{
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
	 * 不抛出编译时异常，简单地进行打印
	 * @param runnerFile
	 */
	public void runFromFileQuietly(File runnerFile)
	{
		try
		{
			runFromFile(runnerFile);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
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
		catch (SAXException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 抛出所有运行时异常，需要调用则自行处理
	 * @param runnerFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InterruptedException
	 * @throws SAXException
	 */
	public void runFromFile(File runnerFile) throws FileNotFoundException, IOException, DocumentException,
		NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
		InterruptedException, SAXException
	{
		if(runnerFile == null)
		{
			new IllegalArgumentException("Runner file param is null.");
			return;
		}
		
		if(!runnerFile.isFile())
		{
			new IllegalArgumentException(String.format("File [%s] is not a file.", runnerFile.getAbsolutePath()));
		}
		
		SuiteParser suiteParser = new SuiteParser();
//		try(InputStream input4Valid = new FileInputStream(runnerFile))
//		{
//			Validation.validationSuite(input4Valid);
//			
//			this.progressInfo.setInfo("文件有效性校验通过！");
//		}

		try(InputStream input = new FileInputStream(runnerFile))
		{
			Suite suite = suiteParser.parse(input);
			
			this.progressInfo.setInfo("文件解析完成！");
			
			suite.setPathUrl(runnerFile.toURI().toURL());
			
			this.progressInfo.setInfo("准备运行套件！");
			
			runSuite(suite);
		}
		finally
		{
			this.progressInfo.setInfo("套件运行完毕！");
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
	private void runSuite(Suite suite)
			throws IOException, DocumentException, NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException, SAXException
	{
		String xmlConfPath = suite.getXmlConfPath();
		if(StringUtils.isBlank(xmlConfPath))
		{
			throw new RuntimeException("Suite xml config is emtpy!");
		}
		
		URL suitePathUrl = suite.getPathUrl();
		try(SettingUtil settingUtil = new SettingUtil())
		{
			String[] xmlConfArray = xmlConfPath.split(",");
			for(String xmlConf : xmlConfArray)
			{
				this.progressInfo.setInfo(String.format("解析元素定位配置文件[%s]！", xmlConf));
				
				if(suite.getPathUrl() != null)
				{
					File patentFile = new File(URLDecoder.decode(suitePathUrl.getFile(), "utf-8"));
					patentFile = patentFile.getParentFile();
					
					settingUtil.readFromSystemPath(new File(patentFile, xmlConf).toString());
				}
				else
				{
					settingUtil.readFromClassPath(xmlConf);
				}
			}
			
			settingUtil.getEngine().setProgressId("progress_identify", progressInfo.getIdentify());
			
			List<SuitePage> pageList = suite.getPageList();
			
			//执行指定的数据组（按照数据序列）
			Integer[] rowsArray = suite.getRowsArray();
			for(int row : rowsArray)
			{
				long afterSleep = suite.getAfterSleep();
				
				this.progressInfo.setInfo(String.format("准备使用第[%s]组数据运行套件，然后休眠时间[%s]毫秒！", row, afterSleep));
				
				runSuiteWithData(settingUtil, row, pageList);
				
				Thread.sleep(afterSleep);
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
	private void runSuiteWithData(SettingUtil settingUtil, int row, List<SuitePage> pageList)
			throws SecurityException, IllegalArgumentException, IllegalAccessException, InterruptedException
	{
		settingUtil.initData(row);
		
		this.progressInfo.setInfo(String.format("数据初始化完毕！共有[%s]个测试页面！", pageList.size()));
		
		for(SuitePage suitePage : pageList)
		{
			String pageCls = suitePage.getPage();
			Page page = (Page) settingUtil.getPage(pageCls);
			if(page == null)
			{
				this.progressInfo.setInfo(String.format("the page[%s] is null.", pageCls));
				continue;
			}
			
			String url = page.getUrl();
			if(StringUtils.isNotBlank(url))
			{
				page.open();
			}
			
			List<SuiteAction> actionList = suitePage.getActionList();
			if(actionList == null)
			{
				actionList = new ArrayList<SuiteAction>();
			}
			
			this.progressInfo.setInfo(String.format("页面[%s]一共有[%s]个测试动作！开始测试！", pageCls, actionList.size()));
			
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
	private void performActionList(Page page, List<SuiteAction> actionList, SettingUtil settingUtil)
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
					progressInfo.setInfo(String.format("Can not found page [%s].", otherPageStr));
					continue;
				}
				else if(!(pageObj instanceof Page))
				{
					progressInfo.setInfo(String.format("Not the page class [%s].", otherPageStr));
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
				
				progressInfo.setInfo(String.format("Action result : %s.", actionResult));
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
	private String performAction(SuiteAction action, Field pageField, 
			Page page, SettingUtil settingUtil)
			throws IllegalArgumentException, IllegalAccessException
	{
		String name = action.getName();
		String invoker = action.getInvoker();
		
		progressInfo.setInfo(String.format("Field [%s] perform Action [%s].", pageField.getName(), name));
		
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
			case "appendValue":
				Text textAppend = getFieldObj(Text.class, pageField, page);
				if(textAppend != null)
				{
					textAppend.appendValue();
				}
				break;
			case "fillNotBlankValue":
				Text textNotBlank = getFieldObj(Text.class, pageField, page);
				if(textNotBlank != null)
				{
					textNotBlank.fillNotBlankValue();
				}
				break;
			case "upload":
				FileUpload fileUpload = getFieldObj(FileUpload.class, pageField, page);
				if(fileUpload != null)
				{
					actionResult = Boolean.toString(fileUpload.upload());
				}
				break;
			case "randomUpload":
				FileUpload randomFileUpload = getFieldObj(FileUpload.class, pageField, page);
				if(randomFileUpload != null)
				{
					actionResult = Boolean.toString(randomFileUpload.randomUpload());
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
			case "randomSelect":
				Selector randomSelector = getFieldObj(Selector.class, pageField, page);
				if(randomSelector != null)
				{
					actionResult = Boolean.toString(randomSelector.randomSelect());
				}
				break;
			case "hover":
				Button hoverBut = getFieldObj(Button.class, pageField, page);
				hoverBut.hover();
				break;
			case "invoke":
				if(StringUtils.isBlank(invoker))
				{
					throw new RuntimeException(String.format("The action in page [%s] is invoke, "
							+ "but can not found invoker [%s].", page.getClass(), invoker));
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
					
					throw new RuntimeException(String.format("Can not found invoker [%s].", invokeCls));
				}
				catch (SecurityException e)
				{
					e.printStackTrace();
					
					throw new RuntimeException(String.format("Execute invoker [%s] has security error.", invokeCls));
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
				
				default:
					throw new RuntimeException("未知的操作：" + name);
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
	private <T> T getFieldObj(Class<T> type, Field pageField, Object instance)
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
