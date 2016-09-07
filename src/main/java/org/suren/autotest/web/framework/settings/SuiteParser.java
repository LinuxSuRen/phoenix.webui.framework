/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.settings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;
import org.jaxen.SimpleNamespaceContext;
import org.suren.autotest.web.framework.core.suite.Suite;
import org.suren.autotest.web.framework.core.suite.SuiteAction;
import org.suren.autotest.web.framework.core.suite.SuitePage;

/**
 * 测试套件配置文件解析
 * @author suren
 * @date 2016年9月7日 下午9:36:16
 */
public class SuiteParser
{
	private SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
	
	public Suite parse(InputStream suiteInputStream) throws DocumentException
	{
		Document document = new SAXReader().read(suiteInputStream);
		
		simpleNamespaceContext.addNamespace("ns", "http://surenpi.com");
		
		XPath xpath = new DefaultXPath("/ns:suite");
		xpath.setNamespaceContext(simpleNamespaceContext);
		Element suiteEle = (Element) xpath.selectSingleNode(document);
		if (suiteEle == null)
		{
			throw new RuntimeException("can not found suite config.");
		}
		
		Suite suite = new Suite();
		String xmlConfPath = suiteEle.attributeValue("xml");
		suite.setXmlConfPath(xmlConfPath);
		
		List<SuitePage> pageList = new ArrayList<SuitePage>();
		suite.setPageList(pageList);
		
		pagesParse(document, pageList);
		
		return suite;
	}

	/**
	 * 解析page配置
	 * @param document
	 * @param pageList
	 */
	private void pagesParse(Document document, List<SuitePage> pageList)
	{
		XPath xpath = new DefaultXPath("/ns:suite/ns:page");
		xpath.setNamespaceContext(simpleNamespaceContext);
		
		List<Element> pageNodes = xpath.selectNodes(document);
		if(pageNodes == null || pageNodes.size() == 0)
		{
			throw new RuntimeException("can not found page config.");
		}
		
		for(Element pageEle : pageNodes)
		{
			String pageCls = pageEle.attributeValue("class");
			
			xpath = new DefaultXPath("/ns:suite/ns:page/ns:actions");
			xpath.setNamespaceContext(simpleNamespaceContext);
			
			Element actionsEle = (Element) xpath.selectSingleNode(document);
			if(actionsEle == null)
			{
				throw new RuntimeException("can not found actions config.");
			}
			
			String beforeSleep = actionsEle.attributeValue("beforeSleep");
			String afterSleep = actionsEle.attributeValue("afterSleep");
			
			List<SuiteAction> actionList = new ArrayList<SuiteAction>();
			
			SuitePage suitePage = new SuitePage(pageCls);
			suitePage.setActionList(actionList);
			pageList.add(suitePage);
			
			parse(actionList, document);
		}
	}

	/**
	 * @param actionList
	 * @param document
	 */
	private void parse(List<SuiteAction> actionList, Document document)
	{
		DefaultXPath xpath = new DefaultXPath("/ns:suite/ns:page/ns:actions/ns:action");
		xpath.setNamespaceContext(simpleNamespaceContext);
		
		List<Element> actionEleList = xpath.selectNodes(document);
		for(Element actionEle : actionEleList)
		{
			String field = actionEle.attributeValue("field");
			String name = actionEle.attributeValue("name");
			
			SuiteAction suiteAction = new SuiteAction(field, name);
			actionList.add(suiteAction);
		}
	}
}