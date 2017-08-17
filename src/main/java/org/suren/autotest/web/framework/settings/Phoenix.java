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
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.jaxen.SimpleNamespaceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.suren.autotest.web.framework.AutoApplicationConfig;
import org.suren.autotest.web.framework.annotation.AutoApplication;
import org.suren.autotest.web.framework.hook.ShutdownHook;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.spring.AutoModuleScope;
import org.suren.autotest.web.framework.util.BeanUtil;
import org.suren.autotest.web.framework.validation.Validation;
import org.xml.sax.SAXException;

import com.beust.jcommander.JCommander;
import com.surenpi.autotest.datasource.ClasspathResource;
import com.surenpi.autotest.datasource.DataResource;
import com.surenpi.autotest.datasource.DataSource;
import com.surenpi.autotest.datasource.DynamicDataSource;
import com.surenpi.autotest.datasource.FileResource;
import com.surenpi.autotest.report.RecordReportWriter;
import com.surenpi.autotest.report.record.ProjectRecord;
import com.surenpi.autotest.utils.NetUtil;
import com.surenpi.autotest.utils.StringUtils;
import com.surenpi.autotest.webui.Page;
import com.surenpi.autotest.webui.core.AutoTestException;
import com.surenpi.autotest.webui.core.ConfigException;
import com.surenpi.autotest.webui.core.ConfigNotFoundException;
import com.surenpi.autotest.webui.core.PageContext;
import com.surenpi.autotest.webui.core.PageContextAware;
import com.surenpi.autotest.webui.core.WebUIEngine;

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
	private Class<?>[] annotatedClasses;
	
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
	    this.annotatedClasses = annotatedClasses;
	}

	/**
	 * auto注解扫描
	 */
	private void annotationScan()
	{
        SeleniumEngine engine = getEngine();
        
		AnnotationProcess annotationProcess = new AnnotationProcess(this.context, this.dataSourceMap);
		annotationProcess.scan(pageMap, engine);
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
	
	/**
	 * @param params
	 * @return
	 */
	public SeleniumEngine init(String[] params)
	{
	    PhoenixParam phoenixParam = new PhoenixParam();
	    JCommander commander = new JCommander(phoenixParam, params);
	    
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
//          ((AnnotationConfigApplicationContext) context).getBeanFactory().registerScope("autotest", new AutotestScope());
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
//          IPageMXBean pageMXBean = context.getBean(IPageMXBean.class);
//          
//          LocateRegistry.createRegistry(5006);
//          MBeanServer server = ManagementFactory.getPlatformMBeanServer();
//          server.registerMBean(pageMXBean,
//                  new ObjectName("org.suren.autotest.web.framework:type=IPageMXBean"));
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
    
        SeleniumEngine engine = getEngine();
        engine.setDriverStr(phoenixParam.browser);
        engine.setRemoteStr(phoenixParam.remote);
        
        engine.init();
        
        return engine;
    }
	
	/**
	 * 初始化框架
	 * @return 引擎对象实例
	 */
	public SeleniumEngine init()
	{
	    return init(null);
	}
	
	/**
	 * 初始化框架并从数据源中加载数据
	 * @see #init()
	 * @see #initData()
	 */
	public void initWithData()
	{
		init();
		initData();
	}

	/**
	 * 从数据源中加载第一组数据，设置到所有page类中
	 * @see #initData(int)
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
		@SuppressWarnings({ "unchecked", "rawtypes" })
        DataSource<Page> dataSource = (DataSource) dynamicDataSourceMap.get(dataSourceInfo.getType());
		if(dataSource == null)
		{
			throw new AutoTestException("Can not found dataSource by type : " + dataSourceInfo.getType());
		}
		
		String resoucePathName = dataSourceInfo.getResource();
		DataResource clzResource = new ClasspathResource(
				Phoenix.class, resoucePathName);
		try
		{
			if(clzResource.getUrl() == null)
			{
                if(configFile != null)
                {
                    File file = new File(configFile.getParentFile(), dataSourceInfo.getResource());
                    if(!file.isFile())
                    {
                        throw new ConfigNotFoundException("dataSourceFile", file.getAbsolutePath());
                    }
                    
                    clzResource = new FileResource(file);
                }
                else
                {
                    throw new ConfigNotFoundException(String.format("Can not found dataSource file '%s' from classpath.",
                            resoucePathName));
                }
			}
		}
		catch (IOException e)
		{
			logger.error("", e);
		}
		
		dataSource.loadData(clzResource, row, page);
		
		return dataSource;
	}
	
	/**
	 * @return 所有的数据源
	 */
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
	 * @param doc xml文档
	 * @throws DocumentException  xml解析错误
	 * @throws IOException 配置文件读取错误
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

		String url = ele.attributeValue("url");
		if (url != null)
		{
			BeanUtil.set(pageInstance, "url", url);
		}
		String paramPrefix = ele.attributeValue("paramPrefix", "param");
		BeanUtil.set(pageInstance, "paramPrefix", paramPrefix);

		BeanUtil.set(pageInstance, "dataSource", dataSrcClsStr);

		FieldVisitor fieldVisitor = new FieldVisitor(pageInstance, pageClsStr, context);
		ele.accept(fieldVisitor);

		pageMap.put(pageClsStr, (Page) pageInstance);
	}
	
	/**
	 * 先使用类的缩写，再使用类全称来查找
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
	
	@Override
    public <T> T getForm(Class<T> form)
    {
        return context.getBean(form);
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
