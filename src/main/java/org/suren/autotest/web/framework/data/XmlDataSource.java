/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
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
import org.suren.autotest.web.framework.core.ui.AbstractElement;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.CheckBoxGroup;
import org.suren.autotest.web.framework.core.ui.FileUpload;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.util.StringUtils;

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
	
	@Autowired
	private List<DynamicData> dynamicDataList;
	
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
			pagePackage = (pagePackage.trim());
			xpath = new DefaultXPath(String.format("/ns:dataSources[@pagePackage='%s']/ns:dataSource[@pageClass='%s']/ns:page[%s]",
					pagePackage, pageClass.replace(pagePackage + ".", ""), String.valueOf(row)));
			xpath.setNamespaceContext(simpleNamespaceContext);
		}
		else
		{
			xpath = new DefaultXPath(String.format("/ns:dataSources/ns:dataSource[@pageClass='%s']/ns:page[%s]",
					pageClass, String.valueOf(row)));
			xpath.setNamespaceContext(simpleNamespaceContext);
		}
		
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
				
				DynamicData dynamicData = getDynamicDataByType(type);
				if(dynamicData != null)
				{
					value = dynamicData.getValue(value);
				}
				else
				{
					throw new RuntimeException("Not support dynamic data type : " + type);
				}

				Method getterMethod = BeanUtils.findMethod(page.getClass(),
						"get" + fieldName.substring(0, 1).toUpperCase()
								+ fieldName.substring(1));
				if(getterMethod == null)
				{
					throw new RuntimeException(String.format("Page [%s] have not field [%s], please recheck it!", page.getClass(), fieldName));
				}
				
				try
				{
					Object eleObj = getterMethod.invoke(page);
					if(!AbstractElement.class.isAssignableFrom(eleObj.getClass()))
					{
						throw new RuntimeException("Not support field type [" + type + "] in page [" + page.getClass().getName() +"].");
					}
					
					AbstractElement absEle = (AbstractElement) eleObj;
					absEle.putData("DataType", type);
					
					//解析数据源中单个数据项的扩展参数
					parseDataParamList(absEle, node);
					
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
						((CheckBoxGroup) eleObj).setValue(value);
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
					else if(eleObj instanceof Button)
					{
						((Button) eleObj).putData("SEQ_OPER", value);
					}
				}
				catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
				{
					LOGGER.error(e.getMessage(), e);
				}
			}
		});
	}

	/**
	 * 解析数据源中单个数据项的扩展参数
	 * @param absEle
	 * @param node
	 */
	private void parseDataParamList(AbstractElement absEle, Element node)
	{
		@SuppressWarnings("unchecked")
		List<Element> paramEleList = node.elements("param");
		if(CollectionUtils.isEmpty(paramEleList))
		{
			return;
		}
		
		for(Element element : paramEleList)
		{
			String key = element.attributeValue("name");
			String value = element.attributeValue("value");
			absEle.putData(key, value);
		}
	}
	
	/**
	 * 根据类型获取对应的动态数据
	 * @param type
	 * @return
	 */
	private DynamicData getDynamicDataByType(String type)
	{
		for(DynamicData dynamicData : dynamicDataList)
		{
			if(dynamicData.getType().equals(type))
			{
				dynamicData.setData(globalMap);
				return dynamicData;
			}
		}
		return null;
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
		catch (IOException | DocumentException| RuntimeException e)
		{
			LOGGER.error(e.getMessage() + "==" + url.toString(), e);
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
