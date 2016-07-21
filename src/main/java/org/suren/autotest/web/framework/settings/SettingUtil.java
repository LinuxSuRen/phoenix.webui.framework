/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.jaxen.SimpleNamespaceContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.suren.autotest.web.framework.core.ui.AbstractElement;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.data.DataResource;
import org.suren.autotest.web.framework.data.DataSource;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.util.BeanUtil;
import org.suren.autotest.web.framework.validation.Validation;

/**
 * 页面（page）以及数据配置加载
 * @author suren
 * @date Jul 17, 2016 9:01:51 AM
 */
public class SettingUtil
{
	private Map<String, Page>			pageMap	= new HashMap<String, Page>();
	private Map<String, DataSourceInfo>	dataSourceMap = new HashMap<String, DataSourceInfo>();
	private ApplicationContext			context;

	public SettingUtil(String ...packages)
	{
		if(packages == null)
		{
			packages = new String[]{"org.suren"};
		}
		else
		{
			String[] tmpArray = Arrays.copyOf(packages, packages.length + 1);
			tmpArray[packages.length] = "org.suren";
			packages = tmpArray;
		}
		
		context = new AnnotationConfigApplicationContext(packages);
	}

	/**
	 * 从本地文件中读取
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public void read(String filePath) throws Exception
	{
		File file = new File(filePath);
		FileInputStream fis = null;

		try
		{
			fis = new FileInputStream(file);

			read(fis);
		}
		finally
		{
			if (fis != null)
			{
				fis.close();
			}
		}
	}

	/**
	 * 从类路径下读取配置文件
	 * 
	 * @param fileName
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void readFromClassPath(String fileName)
			throws IOException, DocumentException
	{
		InputStream inputStream = null;

		try
		{
			inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
			
			Validation.validationFramework(inputStream); //这里会把流关闭了
			
			inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);

			read(inputStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
		}
	}

	/**
	 * 从流中读取配置文件
	 * 
	 * @param inputStream
	 * @throws DocumentException
	 */
	public void read(InputStream inputStream) throws DocumentException
	{
		Document document = new SAXReader().read(inputStream);

		parse(document);
	}

	/**
	 * 从数据源中加载数据，设置到page类中
	 */
	public void initData()
	{
		Iterator<String> pageIterator = pageMap.keySet().iterator();
		while(pageIterator.hasNext())
		{
			String pageKey = pageIterator.next();
			Page page = pageMap.get(pageKey);
			
			String dataSourceStr = page.getDataSource();
			final DataSourceInfo dataSourceInfo = dataSourceMap.get(dataSourceStr);
			if(dataSourceInfo == null)
			{
				continue;
			}
			
			DataSource dataSource = context.getBean(dataSourceInfo.getType(), DataSource.class);
			
			dataSource.loadData(new DataResource()
			{

				@Override
				public URL getUrl()
				{
					return SettingUtil.class.getClassLoader().getResource(dataSourceInfo.getResource());
				}
			}, page);
		}
	}

	/**
	 * @param document
	 */
	private void parse(Document doc)
	{
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		simpleNamespaceContext.addNamespace("ns", "http://surenpi.com");
		
		XPath xpath = new DefaultXPath("/ns:autotest/ns:engine");
		xpath.setNamespaceContext(simpleNamespaceContext);
		// engine parse progress
		Element engineEle = (Element) xpath.selectSingleNode(doc);
		if (engineEle == null)
		{
			throw new RuntimeException("can not found engine config.");
		}

		String driverStr = engineEle.attributeValue("driver");
		String timeOutStr = engineEle.attributeValue("timeout");
		String fullScreenStr = engineEle.attributeValue("fullScreen");
		String widthStr = engineEle.attributeValue("width");
		String heightStr = engineEle.attributeValue("height");
		try
		{
			SeleniumEngine seleniumEngine = context
					.getBean(SeleniumEngine.class);
			seleniumEngine.setDriverStr(driverStr);
			
			try
			{
				seleniumEngine.setTimeout(Long.parseLong(timeOutStr));
			}
			catch(NumberFormatException e)
			{
				seleniumEngine.setTimeout(5);
			}
			
			try
			{
				seleniumEngine.setWidth(Integer.parseInt(widthStr));
				seleniumEngine.setHeight(Integer.parseInt(heightStr));
			}
			catch(NumberFormatException e){}
			
			seleniumEngine.setFullScreen(Boolean.parseBoolean(fullScreenStr));
			
			seleniumEngine.init();
		}
		catch (NoSuchBeanDefinitionException e)
		{
			e.printStackTrace();
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
					System.err.println("can not found class attribute.");
					continue;
				}

				String dataSrcClsStr = ele.attributeValue("dataSource");

				try
				{
					parse(pageClsStr, dataSrcClsStr, ele);
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		//parse datasources
		xpath = new DefaultXPath("/ns:autotest/ns:dataSources/ns:dataSource");
		xpath.setNamespaceContext(simpleNamespaceContext);
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
	 * TODO
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

			/**
			 * {@inheritDoc}
			 * 
			 * @see org.dom4j.VisitorSupport#visit(org.dom4j.Element)
			 */
			@Override
			public void visit(Element node)
			{
				if (!"field".equals(node.getName()))
				{
					return;
				}

				String fieldName = node.attributeValue("name");
				String type = node.attributeValue("type");
				String byId = node.attributeValue("byId");
				String byCss = node.attributeValue("byCss");
				String byName = node.attributeValue("byName");
				String byXpath = node.attributeValue("byXpath");
				String byLinkText = node.attributeValue("byLinkText");
				String byPartialLinkText = node.attributeValue("byPartialLinkText");
				String byTagName = node.attributeValue("byTagName");
				String data = node.attributeValue("data");
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
							System.err.println(String.format(
									"element [%s] is null, maybe you not set autowired.",
									fieldName));
							return;
						}

						if ("button".equals(type))
						{
							Button button = (Button) ele;

							ele = button;
						}
						else if ("input".equals(type))
						{
							Text text = (Text) ele;
							text.setValue(data);

							ele = text;
						}
						else if ("select".equals(type))
						{
//							Selector selector = (Selector) ele;
						}
						else if("file_upload".equals(type))
						{
//							FileUpload fileUpload = (FileUpload) ele;
						}

						ele.setId(byId);
						ele.setName(byName);
						ele.setTagName(byTagName);
						ele.setXPath(byXpath);
						ele.setCSS(byCss);
						ele.setLinkText(byLinkText);
						ele.setPartialLinkText(byPartialLinkText);
					}
					else
					{
						System.err.println(
								String.format("page cls [%s], field [%s]",
										pageClsStr, fieldName));
					}
				}
				catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InvocationTargetException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (ClassCastException e)
				{
					e.printStackTrace();
					System.err.println(
							String.format("fieldName [%s]", fieldName));
				}

				// Object fieldValue = excelData.getValue(fieldName);
				//
				// BeanUtil.set(pageInstance, fieldName, fieldValue);
			}
		});

		pageMap.put(pageClsStr, (Page) pageInstance);
	}

	public Object getPage(String name)
	{
		return pageMap.get(name);
	}

	@SuppressWarnings("unchecked")
	@Deprecated
	public <T> T getPage(T type)
	{
		return (T) getPage(type.getClass().getName());
	}

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
}
