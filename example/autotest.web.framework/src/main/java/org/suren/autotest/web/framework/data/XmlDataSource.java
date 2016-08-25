/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

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
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;

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

	/**
	 * 加载xml格式的数据到page对象中
	 */
	@Override
	public boolean loadData(DataResource resource, Page page)
	{
		this.page = page;
		URL url = resource.getUrl();
		if(url == null)
		{
			return false;
		}
		
		try(InputStream inputStream = url.openStream())
		{
			parse(inputStream);
		}
		catch (IOException | DocumentException e)
		{
			LOGGER.error(e.getMessage(), e);
		}
		
		return false;
	}

	/**
	 * @param inputStream
	 * @throws DocumentException 
	 */
	private void parse(InputStream inputStream) throws DocumentException
	{
		Document document = new SAXReader().read(inputStream);

		parse(document);
	}
	
	private void parse(Document doc)
	{
		String pageClass = page.getClass().getName();
		SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
		simpleNamespaceContext.addNamespace("ns", "http://surenpi.com");
		
		XPath xpath = new DefaultXPath(String.format("/ns:dataSources/ns:dataSource[@pageClass='%s']/ns:page", pageClass));
		xpath.setNamespaceContext(simpleNamespaceContext);
		@SuppressWarnings("unchecked")
		List<Element> dataSourceList = xpath.selectNodes(doc);
		if (dataSourceList == null)
		{
			throw new RuntimeException("can not found datasource config.");
		}
		
		for(Element dataSource : dataSourceList)
		{
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

					Method getterMethod = BeanUtils.findMethod(page.getClass(),
							"get" + fieldName.substring(0, 1).toUpperCase()
									+ fieldName.substring(1));
					
					try
					{
						Object eleObj = getterMethod.invoke(page);
						if(!(eleObj instanceof Text))
						{
							return;
						}
						
						Text text = (Text) eleObj;
						text.setValue(value);
					}
					catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
					{
						LOGGER.error(e.getMessage(), e);
					}
				}
			});
		}
	}

}
