/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.xpath.DefaultXPath;
import org.jaxen.SimpleNamespaceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.xml.sax.SAXException;

/**
 * 根据元素定位的xml描述信息生成xml格式的运行套件（RunnerSuite）文件
 * @author suren
 * @date 2016年12月14日 上午8:24:36
 */
public class DefaultXmlSuiteRunnerGenerator implements Generator
{
	private static final Logger logger = LoggerFactory.getLogger(DefaultXmlDataSourceGenerator.class);

	private String outputDir;
	
	private Map<String, String> suiteActionMap = new HashMap<String, String>();
	
	private Document suiteRunnerDoc = null;
	
	public DefaultXmlSuiteRunnerGenerator()
	{
		suiteActionMap.put("button", "click");
		suiteActionMap.put("input", "fillValue");
		suiteActionMap.put("select", "select");
	}
	
	@Override
	public void generate(String srcCoding, String outputDir)
	{
		this.outputDir = outputDir;

		String srcPath = srcCoding;
		srcPath = srcPath.replace(".xml", "");
		srcPath = srcPath + "_runner.xml";
		
		ClassLoader clsLoader = this.getClass().getClassLoader();
		URL url = clsLoader.getResource(srcPath);
		InputStream dsInput = null;
		try
		{
			if(url != null)
			{
				dsInput = url.openStream();
			}
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(dsInput != null)
		{
			try
			{
				suiteRunnerDoc = new SAXReader().read(dsInput);
			}
			catch (DocumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			suiteRunnerDoc = DocumentHelper.createDocument();
		}
		
		ClassLoader classLoader = this.getClass().getClassLoader();
		
		//读取主配置文件
		try(InputStream confInput = classLoader.getResourceAsStream(srcCoding))
		{
			read(confInput);
		}
		catch (DocumentException | IOException e)
		{
			logger.error(String.format("Main config [%s] parse process error.", srcCoding), e);
		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		XMLWriter xmlWriter = null;
		if(dsInput != null)
		{
			try
			{
				dsInput.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String outputFileName = null;
		if(url != null)
		{
			outputFileName = new File(url.getFile()).getName();
		}
		else
		{
			outputFileName = new File(srcPath).getName();
		}

		File outputDirFile = new File(outputDir);
		if(!outputDirFile.isDirectory())
		{
			outputDirFile.mkdirs();
		}

		try(OutputStream dsOutput = new FileOutputStream(new File(outputDirFile, outputFileName)))
		{
			xmlWriter = new XMLWriter(dsOutput, OutputFormat.createPrettyPrint());
			
			xmlWriter.write(suiteRunnerDoc);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * @param document
	 */
	private void parse(Document doc)
	{
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		simpleNamespaceContext.addNamespace("ns", "http://surenpi.com");
		
		XPath xpath = new DefaultXPath("/ns:autotest/ns:includePage");
		xpath.setNamespaceContext(simpleNamespaceContext);
		@SuppressWarnings("unchecked")
		List<Element> includePageList = xpath.selectNodes(doc);
		if(includePageList != null && includePageList.size() > 0)
		{
			for(Element includePage : includePageList)
			{
				String pageConfig = includePage.attributeValue("pageConfig");
				
				generate(pageConfig, outputDir);
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

				try
				{
					parse(doc, pageClsStr, ele);
				}
				catch (NoSuchBeanDefinitionException e)
				{
					logger.error("Page element [{}] parse error, in document [{}].", "pageClsStr", doc);
					throw e;
				}
				catch (Exception e)
				{
					logger.error("Page element parse error.", e);
				}
			}
		}
	}

	/**
	 * 解析页面Page对象
	 * 
	 * @param pageClsStr
	 * @param dataSrc
	 * @param ele
	 */
	private void parse(Document doc, final String pageClsStr,
			Element ele) throws Exception
	{
		updateXmlDataSourceByEle(ele, pageClsStr);
	}

	/**
	 * @param ele
	 */
	private void updateXmlDataSourceByEle(Element ele, String pageClsStr)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		suiteRunnerDoc.addComment("Auto created by AutoTest, " + dateFormat.format(new Date()));
		
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		simpleNamespaceContext.addNamespace("ns", "http://suite.surenpi.com");
		
		XPath xpath = new DefaultXPath("/ns:suite");
		xpath.setNamespaceContext(simpleNamespaceContext);
		
		//先查找是否有该标签
		Element dataSourcesEle = suiteRunnerDoc.getRootElement();//(Element) xpath.selectSingleNode(suiteRunnerDoc);
		if(dataSourcesEle == null)
		{
			String prefix = "suren";
			dataSourcesEle = suiteRunnerDoc.addElement(prefix + ":suite");
			
			dataSourcesEle.addNamespace(prefix, "http://suite.surenpi.com");
			dataSourcesEle.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			dataSourcesEle.addAttribute("xsi:schemaLocation", "http://suite.surenpi.com "
					+ "http://surenpi.com/schema/suite/autotest.web.framework.suite.xsd ");
		}
		
		xpath = new DefaultXPath("/ns:suite/ns:page[@class='" + pageClsStr + "']");
		xpath.setNamespaceContext(simpleNamespaceContext);
		Element dataSourceEle = (Element) xpath.selectSingleNode(suiteRunnerDoc);
		if(dataSourceEle == null)
		{
			String prefix = dataSourcesEle.getNamespacePrefix();
			if(StringUtils.isBlank(""))
			{
				String parentName = dataSourcesEle.getName();
				if(parentName.contains(":"))
				{
					prefix = parentName.split(":")[0];
				}
			}
			
			if(StringUtils.isNotBlank(prefix))
			{
				prefix = prefix + ":";
			}
			
			dataSourceEle = dataSourcesEle.addElement(prefix + "page");
			
			dataSourceEle.addAttribute("class", pageClsStr);
		}
		
		//只更新第一个子标签
		
		xpath = new DefaultXPath("/ns:suite/ns:page[@class='" + pageClsStr + "']/ns:actions[1]");
		xpath.setNamespaceContext(simpleNamespaceContext);
		Element pageEle = (Element) xpath.selectSingleNode(dataSourceEle);
		if(pageEle == null)
		{
			String prefix = dataSourceEle.getNamespacePrefix();
			if(StringUtils.isNotBlank(prefix))
			{
				prefix = prefix + ":";
			}
			
			pageEle = dataSourceEle.addElement(prefix + "actions");
		}
		
		ele.accept(new PageFieldVisitor(pageEle));
	}

	class PageFieldVisitor extends VisitorSupport
	{
		private SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		private Element actionsEle;
		
		public PageFieldVisitor(Element pageEle)
		{
			this.actionsEle = pageEle;
			simpleNamespaceContext.addNamespace("ns", "http://suite.surenpi.com");
		}

		@Override
		public void visit(Element node)
		{
			if (!"field".equals(node.getName()))
			{
				return;
			}

			String fieldName = node.attributeValue("name");
			String type = node.attributeValue("type");
			String action = suiteActionMap.get(type);
			if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(action))
			{
				return;
			}
			
			XPath xpath = new DefaultXPath("//ns:action[@field='" + fieldName + "']");
			xpath.setNamespaceContext(simpleNamespaceContext);
			
			Element fieldEle = (Element) xpath.selectSingleNode(actionsEle);
			if(fieldEle == null)
			{
				String prefix = actionsEle.getNamespacePrefix();
				if(StringUtils.isNotBlank(prefix))
				{
					prefix = prefix + ":";
				}
				
				fieldEle = actionsEle.addElement(prefix + "action");
				
				fieldEle.addAttribute("field", fieldName);
			}
			
			fieldEle.addAttribute("name", action);
		}
	}
}
