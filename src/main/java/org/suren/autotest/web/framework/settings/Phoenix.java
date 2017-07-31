/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.settings;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.jaxen.SimpleNamespaceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.suren.autotest.web.framework.AutoApplicationConfig;
import org.suren.autotest.web.framework.annotation.AutoApplication;
import org.suren.autotest.web.framework.annotation.AutoDataSource;
import org.suren.autotest.web.framework.annotation.AutoLocator;
import org.suren.autotest.web.framework.annotation.AutoLocators;
import org.suren.autotest.web.framework.annotation.AutoPage;
import org.suren.autotest.web.framework.hook.ShutdownHook;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.spring.AutoModuleScope;
import org.suren.autotest.web.framework.util.BeanUtil;
import org.suren.autotest.web.framework.util.NetUtil;
import org.suren.autotest.web.framework.util.StringUtils;
import org.suren.autotest.web.framework.validation.Validation;
import org.xml.sax.SAXException;

import com.surenpi.autotest.datasource.ClasspathResource;
import com.surenpi.autotest.datasource.DataResource;
import com.surenpi.autotest.datasource.DataSource;
import com.surenpi.autotest.datasource.DynamicDataSource;
import com.surenpi.autotest.datasource.FileResource;
import com.surenpi.autotest.report.RecordReportWriter;
import com.surenpi.autotest.report.record.ProjectRecord;
import com.surenpi.autotest.webui.Page;
import com.surenpi.autotest.webui.core.AutoTestException;
import com.surenpi.autotest.webui.core.ConfigException;
import com.surenpi.autotest.webui.core.ConfigNotFoundException;
import com.surenpi.autotest.webui.core.Locator;
import com.surenpi.autotest.webui.core.LocatorAware;
import com.surenpi.autotest.webui.core.LocatorType;
import com.surenpi.autotest.webui.core.PageContext;
import com.surenpi.autotest.webui.core.PageContextAware;
import com.surenpi.autotest.webui.core.WebUIEngine;
import com.surenpi.autotest.webui.ui.AbstractElement;
import com.surenpi.autotest.webui.ui.Text;

import net.sf.json.util.JSONUtils;

/**
 * 页面（page）以及数据配置加载
 * @author suren
 * @since Jul 17, 2016 9:01:51 AM
 */
public class Phoenix implements Closeable, WebUIEngine
{
	private static final Logger logger = LoggerFactory.getLogger(Phoenix.class);
	
	private Map<String, Page>			pageMap	= new HashMap<String, Page>();
	private Map<String, DataSourceInfo>	dataSourceMap = new HashMap<String, DataSourceInfo>();
	private Map<String, DynamicDataSource> dynamicDataSourceMap;
	private ApplicationContext			context;
	private ShutdownHook shutdownHook;
	
	/** 系统配置路径 */
	private File configFile;
	
	private boolean closed = false;
	
	/** 不希望被加载数据的Page集合 */
	private Set<String> excludePageSet = new HashSet<String>();

	/**
	 * 初始化bean容器，初始化上下文对象，增加JMX管理模块，增加程序关闭钩子。
	 */
	public Phoenix(Class<?> ... annotatedClasses)
	{
		context = SpringUtils.getApplicationContext();
		if(context == null || !((AbstractApplicationContext) context).isActive())
		{
		    if(annotatedClasses == null)
			{
				annotatedClasses = new Class[]{AutoApplicationConfig.class};
			}
			else
			{
			    int len = annotatedClasses.length;
				annotatedClasses = Arrays.copyOf(annotatedClasses, len + 1);
				annotatedClasses[len] = AutoApplicationConfig.class;
			}
			context = new AnnotationConfigApplicationContext(annotatedClasses);
//			((AnnotationConfigApplicationContext) context).getBeanFactory().registerScope("autotest", new AutotestScope());
			Map<String, RecordReportWriter> reportWriters = context.getBeansOfType(RecordReportWriter.class);

			((AnnotationConfigApplicationContext) context).getBeanFactory().registerScope("module",
					new AutoModuleScope(reportWriters.values().parallelStream().collect(Collectors.toList()),
							this));
		}
		
		//auto注解扫描
		annotationScan();
		
		try
		{
			//设置页面上下文对象
			Map<String, PageContextAware> pageContextAwareList =
					context.getBeansOfType(PageContextAware.class);
			if(pageContextAwareList != null)
			{
				for(PageContextAware ware : pageContextAwareList.values())
				{
					ware.setPageContext(new PageContext(pageMap));
				}
			}
			
			//提供运行时管理功能
//			IPageMXBean pageMXBean = context.getBean(IPageMXBean.class);
//			
//			LocateRegistry.createRegistry(5006);
//			MBeanServer server = ManagementFactory.getPlatformMBeanServer();
//			server.registerMBean(pageMXBean,
//					new ObjectName("org.suren.autotest.web.framework:type=IPageMXBean"));
		}
		catch(Exception e)
		{
			logger.error("jmx register process error.", e);
		}
		
		shutdownHook = new ShutdownHook(this);
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		Map<String, Object> autoApp = context.getBeansWithAnnotation(AutoApplication.class);
		final String projectName;
		final String projectDesc;
		if(autoApp != null && autoApp.size() > 0)
		{
			Object appObj = autoApp.values().iterator().next();
			AutoApplication autoApplication = appObj.getClass().getAnnotation(AutoApplication.class);
			if(autoApplication == null)
			{
				autoApplication = appObj.getClass().getSuperclass().getAnnotation(AutoApplication.class);
			}
			projectName = autoApplication.name();
			projectDesc = autoApplication.description();
		}
		else
		{
			projectName = "none";
			projectDesc = "none";
		}

		Map<String, RecordReportWriter> writers = context.getBeansOfType(RecordReportWriter.class);
		if(writers != null)
		{
		    writers.forEach((name, writer) -> {
				ProjectRecord projectRecord = new ProjectRecord();
				projectRecord.setName(projectName);
				projectRecord.setDescription(projectDesc);
				projectRecord.setBrowserInfo("browserInfo");
				projectRecord.setOsName(System.getProperty("os.name"));
				projectRecord.setOsArch(System.getProperty("os.arch"));
				projectRecord.setOsVersion(System.getProperty("os.version"));
				projectRecord.setCountry(System.getProperty("user.country"));
				projectRecord.setLanguage(System.getProperty("user.language"));
				projectRecord.setTimezone(System.getProperty("user.timezone"));
				projectRecord.setAddressInfo(JSONUtils.valueToString(NetUtil.allIP()));
		    	writer.write(projectRecord);
			});
		}
		
		logger.info("init process done.");
	}

	/**
	 * auto注解扫描
	 */
	private void annotationScan()
	{
		Map<String, Object> beanMap = context.getBeansWithAnnotation(AutoPage.class);
		beanMap.forEach((name, bean) -> {
			if(!(bean instanceof Page))
			{
				return;
			}

			Page pageBean = (Page) bean;
			Class<?> beanCls = bean.getClass();
			if(Enhancer.isEnhanced(beanCls))
			{
				beanCls = beanCls.getSuperclass();
			}
			
			String clsName = beanCls.getName();
			pageMap.put(clsName, (Page) bean);

			//页面起始地址处理
			AutoPage autoPageAnno = beanCls.getAnnotation(AutoPage.class);
			String url = autoPageAnno.url();
			pageBean.setUrl(url);

			//设置浏览器信息
			SeleniumEngine engine = getEngine();
			engine.setWidth(autoPageAnno.width());
			engine.setHeight(autoPageAnno.height());
			engine.setMaximize(autoPageAnno.maximize());
			engine.setDriverStr(autoPageAnno.browser());

			//数据源处理
			autoDataSourceProcess(beanCls, pageBean);
			
			//属性上的注解处理
			fieldAnnotationProcess(pageBean);
		});
	}

    /**
     * 数据源处理
     * @param beanCls Page类的class类型
     * @param pageBean Page类对象
     */
    private void autoDataSourceProcess(Class<?> beanCls, Page pageBean)
    {
        AutoDataSource autoDataSource = beanCls.getAnnotation(AutoDataSource.class);
        if(autoDataSource != null)
        {
            String dsName = StringUtils.defaultIfBlank(autoDataSource.name(),
            		System.currentTimeMillis());
            pageBean.setDataSource(dsName);
            dataSourceMap.put(dsName,
            		new DataSourceInfo(autoDataSource.type(), autoDataSource.resource()));
        }
    }

    /**
     * 属性上的注解处理
     * @param bean Page类
    */
    private void fieldAnnotationProcess(Page bean)
    {
		Class<?> beanCls = bean.getClass();
		Field[] fields = beanCls.getDeclaredFields();
		if(!beanCls.getSuperclass().equals(Page.class))
		{
			Field[] superClsFields = beanCls.getSuperclass().getDeclaredFields();
			int oldSize = fields.length;
			int newSize = oldSize + superClsFields.length;
			
			fields = Arrays.copyOf(fields, newSize);
			for(int i = oldSize; i < newSize; i++)
			{
				fields[i] = superClsFields[i - oldSize];
			}
		}
		
		for(Field field : fields)
		{
			AbstractElement element = null;
			
			field.setAccessible(true);
			try
			{
				Object fieldObj = field.get(bean);
				if(!(fieldObj instanceof AbstractElement))
				{
					continue;
				}
				
				element = (AbstractElement) fieldObj;
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
			
			AutoLocator autoLocator = field.getAnnotation(AutoLocator.class);
			if(autoLocator != null)
			{
				LocatorType locatorType = autoLocator.locator();
				String value = autoLocator.value();
				long timeout = autoLocator.timeout();

				element.setParamPrefix("param");
				element.setTimeOut(timeout);
				switch (locatorType)
				{
					case BY_ID:
						element.setId(value);
						break;
					case BY_NAME:
						element.setName(value);
						break;
					case BY_CSS:
						element.setCSS(value);
						break;
					case BY_LINK_TEXT:
						element.setLinkText(value);
						break;
					case BY_PARTIAL_LINK_TEXT:
						element.setPartialLinkText(value);
						break;
					case BY_XPATH:
						element.setXPath(value);
						break;
				}
			}

			AutoLocators autoLocators = field.getAnnotation(AutoLocators.class);
			if(autoLocators != null)
			{
				element.setStrategy(autoLocators.strategy().getName());
				for(AutoLocator locator : autoLocators.locators())
				{
					Map<String, Locator> beans = context.getBeansOfType(Locator.class);
					Collection<Locator> locatorList = beans.values();
					for(Locator locatorItem : locatorList)
					{
						if(!locator.locator().getName().equals(locatorItem.getType()))
						{
							continue;
						}
						
						if(locatorItem instanceof LocatorAware)
						{
							LocatorAware locatorAware = (LocatorAware) locatorItem;
							locatorAware.setValue(locator.value());
							locatorAware.setTimeout(locator.timeout());
							locatorAware.setExtend("");
							
							element.getLocatorList().add(locatorItem);
							
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * 从本地文件中读取
	 * 
	 * @param filePath filePath
	 * @throws DocumentException DocumentException
	 * @throws IOException IOException
	 * @throws SAXException SAXException
	 */
	public void read(String filePath) throws DocumentException, IOException, SAXException
	{
		File file = new File(filePath);
		try(FileInputStream fis = new FileInputStream(file))
		{
			read(fis);
		}
	}

	/**
	 * 从类路径下读取配置文件
	 * 
	 * @param fileName fileName
	 * @throws IOException IOException
	 * @throws DocumentException xml文件解析错误
	 * @throws SAXException xml文件格式校验错误
	 */
	public void readFromClassPath(String fileName)
			throws IOException, DocumentException, SAXException
	{
		ClassLoader classLoader = this.getClass().getClassLoader();
		
		try(InputStream inputStream = classLoader.getResourceAsStream(fileName))
		{
			if(inputStream == null)
			{
				throw new ConfigNotFoundException("ClassPath", fileName);
			}
			
			Validation.validationFramework(inputStream); //这里会把流关闭了
		}
		catch (SAXException | IOException e)
		{
			logger.error("Framework validation process error.", e);
			throw e;
		}
		
		//读取主配置文件
		try(InputStream confInput = classLoader.getResourceAsStream(fileName))
		{
			read(confInput);
		}
		catch (DocumentException | IOException e)
		{
			logger.error(String.format("Main config [%s] parse process error.", fileName), e);
			throw e;
		}
	}
	
	/**
	 * 从操作系统路径中加载配置文件
	 * @param filePath filePath
	 * @throws IOException  IOException
	 * @throws FileNotFoundException  FileNotFoundException
	 * @throws DocumentException  DocumentException
	 * @throws SAXException SAXException
	 */
	public void readFromSystemPath(String filePath) throws FileNotFoundException, 
		IOException, DocumentException, SAXException
	{
		configFile = new File(filePath);
		if(!configFile.isFile())
		{
			throw new ConfigException(String.format("Target file [%s] is not a file.", filePath), "");
		}
		else if(!filePath.endsWith(".xml"))
		{
			logger.warn("Target file [%s] is not end with .xml", filePath);
		}
		
		try(InputStream configInput = new FileInputStream(configFile))
		{
			read(configInput);
		}
	}

	/**
	 * 从流中读取配置文件
	 * 
	 * @param inputStream inputStream
	 * @throws DocumentException DocumentException
	 * @throws IOException IOException
	 * @throws SAXException SAXException
	 */
	public void read(InputStream inputStream) throws DocumentException, IOException, SAXException
	{
		Document document = new SAXReader().read(inputStream);

		parse(document);
	}
	
	public SeleniumEngine init()
	{
		SeleniumEngine engine = getEngine();
		engine.init();
		
		return engine;
	}
	
	public void initWithData()
	{
		init();
		initData();
	}

	/**
	 * 从数据源中加载第一组数据，设置到所有page类中
	 */
	public void initData()
	{
		initData(1);
	}
	
	/**
	 * 从数据源中加载指定的数据组，设置到所有page类中
	 * @param row 数据组序号（从1开始）
	 * @return 如果没有数据源，则返回空列表
	 */
	public List<DynamicDataSource> initData(int row)
	{
		List<DynamicDataSource> dynamicDataSourceList = new ArrayList<DynamicDataSource>();
		Iterator<String> pageIterator = pageMap.keySet().iterator();
		while(pageIterator.hasNext())
		{
			String pageKey = pageIterator.next();
			if(containExcludePage(pageKey))
			{
				logger.warn(String.format("Page [%s] has been exclude, "
						+ "ignore for init data [%s]!", pageKey, row));
				continue;
			}
			
			Page page = pageMap.get(pageKey);
			
			DynamicDataSource dynamicDataSource = initPageData(page, row);
			if(dynamicDataSource != null)
			{
				dynamicDataSourceList.add(dynamicDataSource);
			}
		}
		
		return dynamicDataSourceList;
	}
	
	/**
	 * 从数据源中加载指定的数据组到指定的Page类中
	 * @param page 页面对象
	 * @param row 数据组序号（从1开始）
	 * @return 数据源
	 */
	public DynamicDataSource initPageData(Page page, int row)
	{
		String dataSourceStr = page.getDataSource();
		if(StringUtils.isBlank(dataSourceStr))
		{
			return null;
		}
		
		final DataSourceInfo dataSourceInfo = dataSourceMap.get(dataSourceStr);
		if(dataSourceInfo == null)
		{
			return null;
		}
		
		getDynamicDataSources();
		DataSource dataSource = (DataSource) dynamicDataSourceMap.get(dataSourceInfo.getType());//context.getBean(dataSourceInfo.getType(), DataSource.class);
		if(dataSource == null)
		{
			throw new AutoTestException("Can not found dataSource by type : " + dataSourceInfo.getType());
		}
		
		DataResource clzResource = new ClasspathResource(
				Phoenix.class, dataSourceInfo.getResource());
		try
		{
			if(clzResource.getUrl() == null)
			{
				File file = new File(configFile.getParentFile(), dataSourceInfo.getResource());
				if(!file.isFile())
				{
					throw new RuntimeException("Can not found datasource file : " + file.getAbsolutePath());
				}
				
				clzResource = new FileResource(file);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		dataSource.loadData(clzResource, row, page);
		
		return dataSource;
	}
	
	public Collection<DynamicDataSource> getDynamicDataSources()
	{
		if(dynamicDataSourceMap == null)
		{
			dynamicDataSourceMap = context.getBeansOfType(DynamicDataSource.class);
		}
		
		if(dynamicDataSourceMap != null)
		{
			return dynamicDataSourceMap.values();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 添加Page到要排除的集合中
	 * @param pageCls pageCls
	 */
	public void addExcludePage(String pageCls)
	{
		excludePageSet.add(pageCls);
	}
	
	/**
	 * 从要排除的Page集合中移除
	 * @param pageCls pageCls
	 */
	public void removeExcludePage(String pageCls)
	{
		excludePageSet.remove(pageCls);
	}
	
	/**
	 * 是否包含在要排除的Page集合中
	 * @param pageCls pageCls
	 * @return pageCls
	 */
	public boolean containExcludePage(String pageCls)
	{
		return excludePageSet.contains(pageCls);
	}
	
	/**
	 * 清空要排除的Page集合
	 */
	public void clearExcludePage()
	{
		excludePageSet.clear();
	}
	
	/**
	 * @return 引擎对象
	 */
	public SeleniumEngine getEngine()
	{
		return context.getBean(SeleniumEngine.class);
	}

	/**
	 * 解析整个框架主配置文件
	 * @param doc doc
	 * @throws DocumentException  DocumentException
	 * @throws IOException IOException
	 * @throws SAXException 配置文件格式错误 
	 */
	private void parse(Document doc) throws IOException, DocumentException, SAXException
	{
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		simpleNamespaceContext.addNamespace("ns", "http://surenpi.com");
		
		SeleniumEngine seleniumEngine = getEngine();
		if(StringUtils.isBlank(seleniumEngine.getDriverStr()))
		{
			XPath xpath = new DefaultXPath("/ns:autotest/ns:engine");
			xpath.setNamespaceContext(simpleNamespaceContext);
			// engine parse progress
			Element engineEle = (Element) xpath.selectSingleNode(doc);
			if (engineEle == null)
			{
				throw new RuntimeException("can not found engine config.");
			}
			
			String driverStr = engineEle.attributeValue("driver", "chrome");
			String remoteStr = engineEle.attributeValue("remote");
			String timeOutStr = engineEle.attributeValue("timeout");
			String fullScreenStr = engineEle.attributeValue("fullScreen", "false");
			String maximizeStr = engineEle.attributeValue("maximize", "true");
			String widthStr = engineEle.attributeValue("width");
			String heightStr = engineEle.attributeValue("height");
			try
			{
				seleniumEngine.setDriverStr(driverStr);
				seleniumEngine.setRemoteStr(remoteStr);
				
				try
				{
					seleniumEngine.setTimeout(Long.parseLong(timeOutStr));
				}
				catch(NumberFormatException e)
				{
					logger.warn(String.format("Invalid number string [%s].", timeOutStr));
					seleniumEngine.setTimeout(5);
				}
				
				try
				{
					seleniumEngine.setWidth(Integer.parseInt(widthStr));
					seleniumEngine.setHeight(Integer.parseInt(heightStr));
				}
				catch(NumberFormatException e)
				{
					logger.warn(
							String.format("Invalid number width [%s] or height [%s].",
									widthStr, heightStr));
				}
				
				seleniumEngine.setFullScreen(Boolean.parseBoolean(fullScreenStr));
				seleniumEngine.setMaximize(Boolean.parseBoolean(maximizeStr));
				
				seleniumEngine.init();
			}
			catch (NoSuchBeanDefinitionException e)
			{
				logger.error("Can not found bean SeleniumEngine.", e);
			}
		}
		
		XPath xpath = new DefaultXPath("/ns:autotest/ns:includePage");
		xpath.setNamespaceContext(simpleNamespaceContext);
		@SuppressWarnings("unchecked")
		List<Element> includePageList = xpath.selectNodes(doc);
		if(includePageList != null && includePageList.size() > 0)
		{
			for(Element includePage : includePageList)
			{
				String pageConfig = includePage.attributeValue("pageConfig");
				
				readFromClassPath(pageConfig);
			}
		}
		
		xpath = new DefaultXPath("/ns:autotest/ns:pages");
		xpath.setNamespaceContext(simpleNamespaceContext);
		Element pagesEle = (Element) xpath.selectSingleNode(doc);
		String pagePackage = pagesEle.attributeValue("pagePackage", "");
		if(StringUtils.isNotBlank(pagePackage))
		{
			pagePackage = (pagePackage.trim() + ".");
		}

		// pages parse progress
		xpath = new DefaultXPath("/ns:autotest/ns:pages/ns:page");
		xpath.setNamespaceContext(simpleNamespaceContext);
		@SuppressWarnings("unchecked")
		List<Element> pageNodes = xpath.selectNodes(doc);
		if (pageNodes != null)
		{
			for (Element ele : pageNodes)
			{
				String pageClsStr = ele.attributeValue("class");
				if (pageClsStr == null)
				{
					logger.warn("can not found class attribute.");
					continue;
				}

				pageClsStr = (pagePackage + pageClsStr);
				String dataSrcClsStr = ele.attributeValue("dataSource");

				try
				{
					parse(pageClsStr, dataSrcClsStr, ele);
				}
				catch (NoSuchBeanDefinitionException e)
				{
					logger.error("Page element [{}] parse error, in document [{}].", pageClsStr, doc);
					throw e;
				}
				catch (Exception e)
				{
					logger.error("Page element parse error.", e);
				}
			}
		}
		
		//parse datasources
		xpath = new DefaultXPath("/ns:autotest/ns:dataSources/ns:dataSource");
		xpath.setNamespaceContext(simpleNamespaceContext);
		@SuppressWarnings("unchecked")
		List<Element> dataSourceNodes = xpath.selectNodes(doc);
		if(dataSourceNodes != null)
		{
			for(Element ele : dataSourceNodes)
			{
				String name = ele.attributeValue("name");
				String type = ele.attributeValue("type");
				String resource = ele.attributeValue("resource");
				
				dataSourceMap.put(name, new DataSourceInfo(type, resource));
			}
		}
	}

	/**
	 * 解析页面Page对象
	 * 
	 * @param pageClsStr pageClsStr
	 * @param dataSrcClsStr dataSrcClsStr
	 * @param ele ele
	 */
	private void parse(final String pageClsStr, String dataSrcClsStr,
			Element ele) throws Exception
	{
		final Object pageInstance = getBean(pageClsStr);
		final Class<?> pageCls = pageInstance.getClass();

		String url = ele.attributeValue("url");
		if (url != null)
		{
			BeanUtil.set(pageInstance, "url", url);
		}
		String paramPrefix = ele.attributeValue("paramPrefix", "param");
		BeanUtil.set(pageInstance, "paramPrefix", paramPrefix);

		BeanUtil.set(pageInstance, "dataSource", dataSrcClsStr);

		ele.accept(new VisitorSupport()
		{

			@Override
			public void visit(Element node)
			{
				if (!"field".equals(node.getName()))
				{
					return;
				}

				String fieldName = node.attributeValue("name");
				String byId = node.attributeValue("byId");
				String byCss = node.attributeValue("byCss");
				String byName = node.attributeValue("byName");
				String byXpath = node.attributeValue("byXpath");
				String byLinkText = node.attributeValue("byLinkText");
				String byPartialLinkText = node.attributeValue("byPartialLinkText");
				String byTagName = node.attributeValue("byTagName");
				String data = node.attributeValue("data");
				String type = node.attributeValue("type");
				String strategy = node.attributeValue("strategy");
				String paramPrefix = node.attributeValue("paramPrefix", "param");
				String timeOut = node.attributeValue("timeout", "0");
				if (fieldName == null || "".equals(fieldName))
				{
					return;
				}

				AbstractElement ele = null;
				try
				{
					Method getterMethod = BeanUtils.findMethod(pageCls,
							"get" + fieldName.substring(0, 1).toUpperCase()
									+ fieldName.substring(1));
					if (getterMethod != null)
					{
						ele = (AbstractElement) getterMethod
								.invoke(pageInstance);

						if (ele == null)
						{
							logger.error(String.format(
									"element [%s] is null, maybe you not set autowired.",
									fieldName));
							return;
						}

						if ("input".equals(type))
						{
							Text text = (Text) ele;
							text.setValue(data);

							ele = text;
						}

						ele.setId(byId);
						ele.setName(byName);
						ele.setTagName(byTagName);
						ele.setXPath(byXpath);
						ele.setCSS(byCss);
						ele.setLinkText(byLinkText);
						ele.setPartialLinkText(byPartialLinkText);
						ele.setStrategy(strategy);
						ele.setParamPrefix(paramPrefix);
						ele.setTimeOut(Long.parseLong(timeOut));
					}
					else
					{
						logger.error(String.format("page cls [%s], field [%s]",
										pageClsStr, fieldName));
					}
				}
				catch (IllegalAccessException e)
				{
					logger.error(e.getMessage(), e);
				}
				catch (IllegalArgumentException e)
				{
					logger.error(e.getMessage(), e);
				}
				catch (InvocationTargetException e)
				{
					logger.error(e.getMessage(), e);
				}
				catch (ClassCastException e)
				{
					logger.error(String.format("fieldName [%s]", fieldName), e);
				}

				node.accept(new FieldLocatorsVisitor(ele));
			}
		});

		pageMap.put(pageClsStr, (Page) pageInstance);
	}
	
	/**
	 * 先使用类的缩写，再使用类全程来查找
	 * @param pageClsStr pageClsStr
	 * @return pageClsStr
	 */
	private Object getBean(String pageClsStr)
	{
		String beanName = convertBeanName(pageClsStr);
		
		Object obj = null;
		
		try
		{
			obj = context.getBean(beanName);
		}
		catch(NoSuchBeanDefinitionException e)
		{
			obj = context.getBean(pageClsStr);
		}
		
		return obj;
	}

	/**
	 * 把类名称转为默认的bean名称
	 * @param pageClsStr pageClsStr
	 * @return pageClsStr
	 */
	private String convertBeanName(final String pageClsStr)
	{
		String result = pageClsStr;
		int index = pageClsStr.lastIndexOf(".");

		if(index < 0)
		{
			result = pageClsStr;
		}
		else
		{
			result = pageClsStr.substring(index + 1);
		}
		
		if(result.length() > 1 && result.charAt(1) >= 'A' && result.charAt(1) <= 'Z')
		{
			return result;
		}
		
		return StringUtils.uncapitalize(result);
	}

	/**
	 * 元素定位器信息解析
	 * @author suren
	 * @since 2016年7月28日 上午8:18:01
	 */
	class FieldLocatorsVisitor extends VisitorSupport
	{

		private AbstractElement absEle;
		
		public FieldLocatorsVisitor(AbstractElement element)
		{
			this.absEle = element;
		}
		
		@Override
		public void visit(Element node)
		{
			if (!"locator".equals(node.getName()))
			{
				return;
			}
			
			String name = node.attributeValue("name");
			String value = node.attributeValue("value");
			String timeoutStr = node.attributeValue("timeout");
			String extend = node.attributeValue("extend");
			
			if(StringUtils.isBlank(name) || StringUtils.isBlank(value))
			{
				logger.error("locator has empty name or value.");
			}
			
			long timeout = 0;
			if(StringUtils.isNotBlank(timeoutStr))
			{
				try
				{
					timeout = Long.parseLong(timeoutStr);
				}
				catch(NumberFormatException e){}
			}
			
			Map<String, Locator> beans = context.getBeansOfType(Locator.class);
			Collection<Locator> locatorList = beans.values();
			for(Locator locator : locatorList)
			{
				if(!name.equals(locator.getType()))
				{
					continue;
				}
				
				if(locator instanceof LocatorAware)
				{
					LocatorAware locatorAware = (LocatorAware) locator;
					locatorAware.setValue(value);
					locatorAware.setTimeout(timeout);
					locatorAware.setExtend(extend);
					
					absEle.getLocatorList().add(locator);
					
					break;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getDriver()
	{
		return (T) getEngine().getDriver();
	}

	/**
	 * @param name name
	 * @return 给定名称的Page对象
	 */
	public Object getPage(String name)
	{
		return pageMap.get(name);
	}

	/**
	 * @param type type
	 * @return 给定类型的Page对象
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public <T> T getPage(T type)
	{
		return (T) getPage(type.getClass().getName());
	}

	/**
	 * @param type type
	 * @return 给定类型的Page对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getPage(Class<T> type)
	{
		return (T) getPage(type.getName());
	}

	@Override
	public <T> T getModule(Class<T> type)
	{
		return context.getBean(type);
	}
	
	class DataSourceInfo
	{
		private String type;
		private String resource;
		public DataSourceInfo(String type, String resource)
		{
			this.type = type;
			this.resource = resource;
		}
		public String getType()
		{
			return type;
		}
		public void setType(String type)
		{
			this.type = type;
		}
		public String getResource()
		{
			return resource;
		}
		public void setResource(String resource)
		{
			this.resource = resource;
		}
	}
	
	@Override
	public void open(String url)
	{
		getEngine().getDriver().get(url);
	}

	/**
	 * 关闭引擎
	 */
	@Override
	public void close() throws IOException
	{
		if(closed)
		{
			return;
		}
		
		SeleniumEngine engine = context.getBean(SeleniumEngine.class);
		if(engine != null)
		{
			engine.close();
			closed = true;
			
			Runtime.getRuntime().removeShutdownHook(shutdownHook);
		}
		else
		{
			logger.error("Can not fond seleniumEngine, resource close failed.");
		}
	}

	/**
	 * 关闭容器
	 */
	public void shutdown()
	{
		((AbstractApplicationContext) context).destroy();
		((AbstractApplicationContext) context).close();
	}

	/**
	 * @return 是否已经关闭
	 */
	public boolean isClosed()
	{
		return closed;
	}
}
