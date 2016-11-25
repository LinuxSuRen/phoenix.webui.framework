/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.settings;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.registry.LocateRegistry;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.eclipse.jetty.util.StringUtil;
import org.jaxen.SimpleNamespaceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.suren.autotest.web.framework.core.ConfigException;
import org.suren.autotest.web.framework.core.ConfigNotFoundException;
import org.suren.autotest.web.framework.core.Locator;
import org.suren.autotest.web.framework.core.LocatorAware;
import org.suren.autotest.web.framework.core.PageContext;
import org.suren.autotest.web.framework.core.PageContextAware;
import org.suren.autotest.web.framework.core.ui.AbstractElement;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.data.ClasspathResource;
import org.suren.autotest.web.framework.data.DataSource;
import org.suren.autotest.web.framework.hook.ShutdownHook;
import org.suren.autotest.web.framework.jmx.IPageMXBean;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.util.BeanUtil;
import org.suren.autotest.web.framework.validation.Validation;
import org.xml.sax.SAXException;

/**
 * 页面（page）以及数据配置加载
 * @author suren
 * @date Jul 17, 2016 9:01:51 AM
 */
public class SettingUtil implements Closeable
{
	private static final Logger logger = LoggerFactory.getLogger(SettingUtil.class);
	
	private Map<String, Page>			pageMap	= new HashMap<String, Page>();
	private Map<String, DataSourceInfo>	dataSourceMap = new HashMap<String, DataSourceInfo>();
	private AbstractApplicationContext			context;
	
	private boolean closed = false;
	
	/** 不希望被加载数据的Page集合 */
	private Set<String> excludePageSet = new HashSet<String>();

	/**
	 * 初始化bean容器，初始化上下文对象，增加JMX管理模块，增加程序关闭钩子。
	 */
	public SettingUtil()
	{
		context = new ClassPathXmlApplicationContext(new String[]{"classpath*:applicationContext.xml"});
		
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
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
		
		logger.info("init process done.");
	}

	/**
	 * 从本地文件中读取
	 * 
	 * @param filePath
	 * @throws DocumentException
	 * @throws IOException
	 * @throws SAXException 
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
	 * @param fileName
	 * @throws IOException
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
	 * @param filePath
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws DocumentException 
	 * @throws SAXException 
	 */
	public void readFromSystemPath(String filePath) throws FileNotFoundException, 
		IOException, DocumentException, SAXException
	{
		File configFile = new File(filePath);
		if(!configFile.isFile())
		{
			throw new ConfigException(String.format("Target file [%s] is not a file.", filePath));
		}
		else if(filePath.endsWith(".xml"))
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
	 * @param inputStream
	 * @throws DocumentException
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public void read(InputStream inputStream) throws DocumentException, IOException, SAXException
	{
		Document document = new SAXReader().read(inputStream);

		parse(document);
	}

	/**
	 * 从数据源中加载第一组数据，设置到所有page类中
	 * @see {@link #initData(int)}
	 */
	public void initData()
	{
		initData(1);
	}
	
	/**
	 * 从数据源中加载指定的数据组，设置到所有page类中
	 * @param 数据组序号（从1开始）
	 */
	public void initData(int row)
	{
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
			initPageData(page, row);
		}
	}
	
	/**
	 * 从数据源中加载指定的数据组到指定的Page类中
	 * @param page
	 * @param row 数据组序号（从1开始）
	 * @return
	 */
	public boolean initPageData(Page page, int row)
	{
		String dataSourceStr = page.getDataSource();
		final DataSourceInfo dataSourceInfo = dataSourceMap.get(dataSourceStr);
		if(dataSourceInfo == null)
		{
			return false;
		}
		
		DataSource dataSource = context.getBean(dataSourceInfo.getType(), DataSource.class);
		ClasspathResource clzResource = new ClasspathResource(
				SettingUtil.class, dataSourceInfo.getResource());
		
		return dataSource.loadData(clzResource, row, page);
	}
	
	/**
	 * 添加Page到要排除的集合中
	 * @param pageCls
	 */
	public void addExcludePage(String pageCls)
	{
		excludePageSet.add(pageCls);
	}
	
	/**
	 * 从要排除的Page集合中移除
	 * @param pageCls
	 */
	public void removeExcludePage(String pageCls)
	{
		excludePageSet.remove(pageCls);
	}
	
	/**
	 * 是否包含在要排除的Page集合中
	 * @param pageCls
	 * @return
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
	 * 解析整个框架主配置文件
	 * @param document
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws SAXException 配置文件格式错误 
	 */
	private void parse(Document doc) throws IOException, DocumentException, SAXException
	{
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		simpleNamespaceContext.addNamespace("ns", "http://surenpi.com");
		
		SeleniumEngine seleniumEngine = context
				.getBean(SeleniumEngine.class);
		if(seleniumEngine.getDriverStr() == null || "".equals(seleniumEngine.getDriverStr()))
		{
			XPath xpath = new DefaultXPath("/ns:autotest/ns:engine");
			xpath.setNamespaceContext(simpleNamespaceContext);
			// engine parse progress
			Element engineEle = (Element) xpath.selectSingleNode(doc);
			if (engineEle == null)
			{
				throw new RuntimeException("can not found engine config.");
			}
			
			String driverStr = engineEle.attributeValue("driver");
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
		if(StringUtil.isNotBlank(pagePackage))
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
				catch (Exception e)
				{
					logger.error("page element parse error.", e);
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
	 * @param pageClsStr
	 * @param dataSrcClsStr
	 * @param ele
	 */
	private void parse(final String pageClsStr, String dataSrcClsStr,
			Element ele) throws Exception
	{
		final Class<?> pageCls = Class.forName(pageClsStr);
		final Object pageInstance = context.getBean(pageCls);

		String url = ele.attributeValue("url");
		if (url != null)
		{
			BeanUtil.set(pageInstance, "url", url);
		}
		
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

				// Object fieldValue = excelData.getValue(fieldName);
				//
				// BeanUtil.set(pageInstance, fieldName, fieldValue);
				
				node.accept(new FieldLocatorsVisitor(ele));
			}
		});

		pageMap.put(pageClsStr, (Page) pageInstance);
	}
	
	/**
	 * 元素定位器信息解析
	 * @author suren
	 * @date 2016年7月28日 上午8:18:01
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

	/**
	 * @param name
	 * @return 给定名称的Page对象
	 */
	public Object getPage(String name)
	{
		return pageMap.get(name);
	}

	/**
	 * @param type
	 * @return 给定类型的Page对象
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public <T> T getPage(T type)
	{
		return (T) getPage(type.getClass().getName());
	}

	/**
	 * @param type
	 * @return 给定类型的Page对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T getPage(Class<T> type)
	{
		return (T) getPage(type.getName());
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
			context.destroy();
			closed = true;
		}
		else
		{
			logger.error("Can not fond seleniumEngine, resource close failed.");
		}
	}

	/**
	 * @return 是否已经关闭
	 */
	public boolean isClosed()
	{
		return closed;
	}
}
