/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.jaxen.SimpleNamespaceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.Callback;
import org.suren.autotest.web.framework.util.StringUtils;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.InvalidReferenceException;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 根据xml来生成Java源码
 * @author suren
 * @date 2016年12月3日 下午8:43:19
 */
@Component("xml_to_java")
public class DefaultXmlCodeGenerator implements Generator
{
	private static final Logger logger = LoggerFactory.getLogger(DefaultXmlCodeGenerator.class);
	
	private Map<String, List<AutoField>> pageFieldMap = new HashMap<String, List<AutoField>>();
	private Map<String, String> pageCommentMap = new HashMap<String, String>();
	
	private Map<String, String> fieldTypeMap = new HashMap<String, String>();
	
	private ClassLoader classLoader;
	private String outputDir;
	
	/**
	 * 默认构造函数
	 */
	public DefaultXmlCodeGenerator()
	{
		fieldTypeMap.put("button", "Button");
		fieldTypeMap.put("input", "Text");
		fieldTypeMap.put("select", "Selector");
		fieldTypeMap.put("file_upload", "FileUpload");
		fieldTypeMap.put("check_box_group", "CheckBoxGroup");
	}

	@Override
	public void generate(String srcCoding, String outputDir)
	{
		this.outputDir = outputDir;
		classLoader = this.getClass().getClassLoader();
		
		if(new File(srcCoding).isFile())
		{
			try(InputStream inputStream = new FileInputStream(new File(srcCoding)))
			{
				SAXReader saxReader = new SAXReader();
				saxReader.setEncoding("utf-8");
				
				Document document = saxReader.read(inputStream);
				
				read(document);
			}
			catch (DocumentException | IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try(InputStream inputStream = classLoader.getResourceAsStream(srcCoding))
			{
				Document document = new SAXReader().read(inputStream);
				
				read(document);
			}
			catch (DocumentException | IOException e)
			{
				e.printStackTrace();
			}
		}
		
		Iterator<String> pageIt = pageFieldMap.keySet().iterator();
		while(pageIt.hasNext())
		{
			String pageCls = pageIt.next();
			
			AutoPage autoPage = new AutoPage();
			int index = pageCls.lastIndexOf(".");
			if(index > 0)
			{
				autoPage.setPackageName(pageCls.substring(0, index));
			}
			autoPage.setName(pageCls.substring(index + 1));
			autoPage.setComment("");
			if(pageCommentMap.containsKey(pageCls))
			{
				autoPage.setComment(pageCommentMap.get(pageCls));
			}
			
			List<AutoField> fieldList = pageFieldMap.get(pageCls);
			for(AutoField autoField : fieldList)
			{
				if(StringUtils.isBlank(autoField.getType()))
				{
					continue;
				}
				
				if(fieldTypeMap.get(autoField.getType()) != null)
				{
					autoField.setType(fieldTypeMap.get(autoField.getType()));
				}
				
				String fieldName = autoField.getName();
				String methodField = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
						
				autoField.setGetterMethod("get" + methodField);
				autoField.setSetterMethod("set" + methodField);
			}
			
			autoPage.setFields(fieldList);
			
			create(autoPage);
		}
		
		done();
	}
	
	@Override
	public void generate(InputStream input, String outputDir, Callback callback)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * 代码生成完毕
	 */
	protected void done()
	{
	}
	
	/**
	 * 根据Page对象来创建Java源文件
	 * @param autoPage
	 */
	protected void create(AutoPage autoPage)
	{
		try
		{
			TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/template");
			
			Configuration configuration = new Configuration();
//			configuration.setDirectoryForTemplateLoading(new File("d:/ftp"));
			configuration.setTemplateLoader(templateLoader);
			configuration.setObjectWrapper(new DefaultObjectWrapper()); 
			configuration.setDefaultEncoding("UTF-8");
			
			Map<String, AutoPage> paramMap = new HashMap<String, AutoPage>();
			paramMap.put("page", autoPage);
			Template template = configuration.getTemplate("page.ftl");
			
			StringBuffer pathBuf = new StringBuffer(outputDir);
			pathBuf.append("/");
			String packageName = autoPage.getPackageName();
			String[] packageArray = packageName.split("\\.");
			if(packageArray != null && packageArray.length > 0)
			{
				for(String pkName : packageArray)
				{
					if(!pkName.equals(""))
					{
						pathBuf.append(pkName).append("/");
					}
				}
			}
			
			String parentDir = pathBuf.toString();
			new File(parentDir).mkdirs();
			
			Writer writer = new OutputStreamWriter(
					new FileOutputStream(parentDir + autoPage.getName() + ".java"),"UTF-8"); 
		    template.process(paramMap, writer);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch(InvalidReferenceException e)
		{
			System.out.println(autoPage);
			e.printStackTrace();
		}
		catch (TemplateException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param document
	 */
	private void read(Document doc)
	{
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		simpleNamespaceContext.addNamespace("ns", "http://surenpi.com");
		
		DefaultXPath xpath = new DefaultXPath("/ns:autotest/ns:pages");
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
				Object commentObj = ele.getData();
				if(commentObj != null)
				{
					pageCommentMap.put(pageClsStr, commentObj.toString().trim());
				}

				try
				{
					List<AutoField> fieldList = new ArrayList<AutoField>();
					
					pageFieldMap.put(pageClsStr, fieldList);
					
					parsePage(pageClsStr, ele, fieldList);
				}
				catch (Exception e)
				{
					logger.error("page element parse error.", e);
				}
			}
		}
	}

	/**
	 * 解析页面Page对象
	 * 
	 * @param pageClsStr
	 * @param dataSrcClsStr
	 * @param ele
	 * @param fieldList 
	 */
	private void parsePage(final String pageClsStr, Element ele, final List<AutoField> fieldList) throws Exception
	{
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
				String type = node.attributeValue("type");
				if (fieldName == null || "".equals(fieldName)
						|| type == null || "".equals(type))
				{
					return;
				}
				
				AutoField autoField = new AutoField(fieldName, type);
				
				Object commentObj = node.getData();
				if(commentObj != null)
				{
					autoField.setComment(commentObj.toString().trim());
				}
				
				fieldList.add(autoField);
			}
		});
	}

	/**
	 * @return 解析配置文件所使用的类加载器
	 */
	public ClassLoader getClassLoader()
	{
		return classLoader;
	}

}
