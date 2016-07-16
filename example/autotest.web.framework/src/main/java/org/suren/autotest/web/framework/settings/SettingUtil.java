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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.suren.autotest.web.framework.core.ui.AbstractElement;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.data.DataFactory;
import org.suren.autotest.web.framework.data.ExcelData;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.util.BeanUtil;

/**
 * 
 * 此处填写类简介
 * <p>
 * 此处填写类说明
 * </p>
 * @author sunyp
 * @since jdk1.6
 * 2016年6月24日
 *  
 */
public class SettingUtil {
	private Map<String, Page> pageMap = new HashMap<String, Page>();
	private ApplicationContext context;
	
	public SettingUtil() {
		context = new AnnotationConfigApplicationContext("org.suren");
	}
	
	/**
	 * 从本地文件中读取
	 * @param filePath
	 * @throws Exception
	 */
	public void read(String filePath) throws Exception {
		File file = new File(filePath);
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(file);
			
			read(fis);
		} finally {
			if(fis != null) {
				fis.close();
			}
		}
	}
	
	/**
	 * 从类路径下读取配置文件
	 * @param fileName
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public void readFromClassPath(String fileName) throws IOException, DocumentException {
		InputStream inputStream = null;
		
		try {
			inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
			
			read(inputStream);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}
	
	/**
	 * 从流中读取配置文件
	 * @param inputStream
	 * @throws DocumentException
	 */
	public void read(InputStream inputStream) throws DocumentException {
		Document document = new SAXReader().read(inputStream);
		
		parse(document);
	}

	/**
	 * @param document
	 */
	private void parse(Document doc) {
		//engine parse progress
		Element engineEle = (Element) doc.selectSingleNode("/autotest/engine");
		if(engineEle == null) {
			throw new RuntimeException("can not found engine config.");
		}
		
		String driverStr = engineEle.attributeValue("driver");
		try {
			SeleniumEngine seleniumEngine = context.getBean(SeleniumEngine.class);
			seleniumEngine.setDriverStr(driverStr);
		} catch (NoSuchBeanDefinitionException e) {
			e.printStackTrace();
		}
		
		//pages parse progress
		List<Element> pageNodes = doc.selectNodes("/autotest/pages/page");
		if(pageNodes != null) {
			for(Element ele : pageNodes) {
				String pageClsStr = ele.attributeValue("class");
				if(pageClsStr == null) {
					System.err.println("can not found class attribute.");
					continue;
				}
				
				String dataSrcClsStr = ele.attributeValue("dataSource");
				
				try {
					parse(pageClsStr, dataSrcClsStr, ele);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * TODO
	 * @param pageClsStr
	 * @param dataSrcClsStr
	 * @param ele
	 */
	private void parse(final String pageClsStr, String dataSrcClsStr, Element ele) throws Exception {
		final Class<?> pageCls = Class.forName(pageClsStr);
		final Object pageInstance = context.getBean(pageCls);
		final ExcelData excelData = (ExcelData) DataFactory.getData(dataSrcClsStr);
		
		String url = ele.attributeValue("url");
		if(url != null) {
			BeanUtil.set(pageInstance, "url", url);
		}
		
		ele.accept(new VisitorSupport() {

			/** 
			  * {@inheritDoc}   
			  * @see org.dom4j.VisitorSupport#visit(org.dom4j.Element) 
			  */
			@Override
			public void visit(Element node) {
				if(!"field".equals(node.getName())) {
					return;
				}
				
				String fieldName = node.attributeValue("name");
				String type = node.attributeValue("type");
				String byId = node.attributeValue("byId");
				String byName = node.attributeValue("byName");
				String byTagName = node.attributeValue("byTagName");
				String byXpath = node.attributeValue("byXpath");
				String byCss = node.attributeValue("byCss");
				String byLinkText = node.attributeValue("byLinkText");
				String byPartialLinkText = node.attributeValue("byPartialLinkText");
				String data = node.attributeValue("data");
				if(fieldName == null || "".equals(fieldName)) {
					return;
				}
				
				AbstractElement ele = null;
				try {
					Method getterMethod = BeanUtils.findMethod(pageCls,
							"get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
					if(getterMethod != null) {
						ele = (AbstractElement) getterMethod.invoke(pageInstance);
						
						if(ele == null) {
							System.err.println(String.format("element [%s] is null, maybe you not set autowired.", fieldName));
							return;
						}
						
						if("button".equals(type)) {
							Button button = (Button) ele;
							
							ele = button;
						} else if("input".equals(type)) {
							Text text = (Text) ele;
							text.setValue(data);
							
							ele = text;
						} else if("select".equals(type)) {
							Selector selector = (Selector) ele;
						}
						
						if(ele != null) {
							ele.setId(byId);
							ele.setName(byName);
							ele.setTagName(byTagName);
							ele.setXPath(byXpath);
							ele.setCSS(byCss);
							ele.setLinkText(byLinkText);
							ele.setPartialLinkText(byPartialLinkText);
						}
					} else {
						System.err.println(String.format("page cls [%s], field [%s]", pageClsStr, fieldName));
					}
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassCastException e) {
					e.printStackTrace();
					System.err.println(String.format("fieldName [%s]", fieldName));
				}
				
//				Object fieldValue = excelData.getValue(fieldName);
//				
//				BeanUtil.set(pageInstance, fieldName, fieldValue);
			}
		});
		
		pageMap.put(pageClsStr, (Page) pageInstance);
	}
	
	public Object getPage(String name) {
		return pageMap.get(name);
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	public <T> T getPage(T type) {
		return (T) getPage(type.getClass().getName());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getPage(Class<T> type) {
		return (T) getPage(type.getName());
	}
}
