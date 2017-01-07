/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilationFailedException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.CheckBoxGroup;
import org.suren.autotest.web.framework.core.ui.FileUpload;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.util.EncryptorUtil;

/**
 * xml格式的数据源实现
 * @author suren
 * @date Jul 17, 2016 8:56:51 AM
 */
@Component("xml_data_source")
public class XmlDataSource implements DataSource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(XmlDataSource.class);
	
	private Page page;
	private Map<String, Object> globalMap = new HashMap<String, Object>();
	
	private static final String groovyCls;
	
	@Autowired
	private DynamicData dynamicData;
	
	static
	{
		StringBuffer buf = new StringBuffer();
		try(InputStream stream = XmlDataSource.class.getClassLoader().getResource("random.groovy").openStream())
		{
			byte[] bf = new byte[1024];
			int len = -1;
			while((len = stream.read(bf)) != -1)
			{
				buf.append(new String(bf, 0, len));
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		groovyCls = buf.toString();
	}
	
	public XmlDataSource()
	{
	}

	/**
	 * 加载xml格式的数据到page对象中
	 */
	@Override
	public boolean loadData(DataResource resource, Page page)
	{
		return loadData(resource, 0, page);
	}

	/**
	 * 解析xml文档
	 * @param inputStream
	 * @param row 
	 * @throws DocumentException 
	 */
	private void parse(InputStream inputStream, int row) throws DocumentException
	{
		Document document = new SAXReader().read(inputStream);

		parse(document, row);
	}
	
	/**
	 * 解析docment对象
	 * @param doc
	 * @param row
	 */
	private void parse(Document doc, int row)
	{
		String pageClass = page.getClass().getName();
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		simpleNamespaceContext.addNamespace("ns", "http://datasource.surenpi.com");
		
		XPath xpath = new DefaultXPath("/ns:dataSources");
		xpath.setNamespaceContext(simpleNamespaceContext);
		Element rootEle = (Element) xpath.selectSingleNode(doc);
		String pagePackage = rootEle.attributeValue("pagePackage", "");
		if(StringUtils.isNotBlank(pagePackage))
		{
			pagePackage = (pagePackage.trim() + ".");
		}
		
		xpath = new DefaultXPath(String.format("/ns:dataSources/ns:dataSource[@pageClass='%s%s']/ns:page[%s]",
				pagePackage, pageClass, String.valueOf(row)));
		xpath.setNamespaceContext(simpleNamespaceContext);
		@SuppressWarnings("unchecked")
		List<Element> dataSourceList = xpath.selectNodes(doc);
		if (dataSourceList == null || dataSourceList.size() == 0)
		{
			throw new RuntimeException(
					String.format("Can not found datasource config by xpath [%s].",
							xpath.toString()));
		}
		
		Element dataSource = dataSourceList.get(0);
		dataSource.accept(new VisitorSupport()
		{

			@Override
			public void visit(Element node)
			{
				if(!"field".equals(node.getName()))
				{
					return;
				}
				
				String fieldName = node.attributeValue("name");
				String value = node.attributeValue("data");
				String type = node.attributeValue("type", "simple");
				String field = node.attributeValue("field", "value");
				String callback = node.attributeValue("callback", "");
				
				LOGGER.debug("Field [{}], value [{}], type [{}], field [{}].", fieldName, value, type, field);
				
				if("simple".equals(type))
				{
					value = dynamicData.getValue(value);
				}
				else if("groovy".equals(type))
				{
					Binding binding = new Binding();
					GroovyShell shell = new GroovyShell(binding);
					
					binding.setVariable("globalMap", XmlDataSource.this.getGlobalMap());
					Object resObj = null;
					try
					{
						resObj = shell.evaluate(groovyCls + value);
						if(resObj != null)
						{
							value = resObj.toString();
						}
						else
					 	{
							value = "groovy not return!";
						}
					}
					catch(CompilationFailedException e)
					{
						value = e.getMessage();
						e.printStackTrace();
					}
				}
				else if("javascript".equals(type))
				{
					ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
					try
					{
						Object resObj = engine.eval(value);
						if(resObj != null)
						{
							value = resObj.toString();
						}
						else
						{
							value = "js not return!";
						}
					}
					catch (ScriptException e)
					{
						value = e.getMessage();
						e.printStackTrace();
					}
				}
				else if("encrypt".equals(type))
				{
					value = EncryptorUtil.decryptWithBase64(value);
				}
				else
				{
					new RuntimeException("Not support type : " + type);
				}

				Method getterMethod = BeanUtils.findMethod(page.getClass(),
						"get" + fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1));
				
				try
				{
					Object eleObj = getterMethod.invoke(page);
					if(eleObj instanceof Text)
					{
						Text text = (Text) eleObj;
						text.setValue(value);
						
						if("callback".equals(type))
						{
							text.setCallback(callback);
						}
					}
					else if(eleObj instanceof CheckBoxGroup)
					{
						((CheckBoxGroup) eleObj).setTargetText(value);
					}
					else if(eleObj instanceof Selector)
					{
						switch(field)
						{
							case "text":
								((Selector) eleObj).setText(value);
								break;
							case "index":
								((Selector) eleObj).setIndex(Integer.parseInt(value));
								break;
							case "value":
								((Selector) eleObj).setValue(value);
								break;
						}
					}
					else if(eleObj instanceof FileUpload)
					{
						((FileUpload) eleObj).setTargetFile(new File(value));
					}
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					LOGGER.error(e.getMessage(), e);
				}
			}
		});
	}

	@Override
	public boolean loadData(DataResource resource, int row, Page page)
	{
		this.page = page;
		URL url = null;
		try {
			url = resource.getUrl();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		
		if(url == null)
		{
			return false;
		}
		
		try(InputStream inputStream = url.openStream()) //打开文件流
		{
			parse(inputStream, row); //解析xml数据源文件
			
			return true;
		}
		catch (IOException | DocumentException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		
		return false;
	}

	/**
	 * @return the globalMap
	 */
	public Map<String, Object> getGlobalMap()
	{
		return globalMap;
	}

	/**
	 * @param globalMap the globalMap to set
	 */
	public void setGlobalMap(Map<String, Object> globalMap)
	{
		this.globalMap = globalMap;
	}

}
