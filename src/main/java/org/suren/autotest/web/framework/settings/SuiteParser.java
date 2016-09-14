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
	
	/**
	 * 解析测试套件配置文件
	 * @param suiteInputStream 配置文件输入流
	 * @return 测试套件对象
	 * @throws DocumentException
	 */
	public Suite parse(InputStream suiteInputStream) throws DocumentException
	{
		Document document = new SAXReader().read(suiteInputStream);
		
		simpleNamespaceContext.addNamespace("ns", "http://suite.surenpi.com");
		
		XPath xpath = new DefaultXPath("/ns:suite");
		xpath.setNamespaceContext(simpleNamespaceContext);
		Element suiteEle = (Element) xpath.selectSingleNode(document);
		if (suiteEle == null)
		{
			throw new RuntimeException("can not found suite config.");
		}
		
		Suite suite = new Suite();
		String xmlConfPath = suiteEle.attributeValue("pageConfig");
		String rows = suiteEle.attributeValue("rows", "1");
		String lackLines = suiteEle.attributeValue("lackLines", "nearby");
		String errorLines = suiteEle.attributeValue("errorLines", "stop");
		String afterSleep = suiteEle.attributeValue("afterSleep", "0");
		
		suite.setXmlConfPath(xmlConfPath);
		suite.setRows(rows);
		suite.setLackLines(lackLines);
		suite.setErrorLines(errorLines);
		suite.setAfterSleep(Long.parseLong(afterSleep));
		
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
			
			xpath = new DefaultXPath("ns:actions");
			xpath.setNamespaceContext(simpleNamespaceContext);
			
			Element actionsEle = (Element) xpath.selectSingleNode(pageEle);
			if(actionsEle == null)
			{
				throw new RuntimeException("can not found actions config.");
			}
			
			String disable = actionsEle.attributeValue("disable", "false");
			if(Boolean.parseBoolean(disable))
			{
				continue;
			}
			
			String beforeSleep = actionsEle.attributeValue("beforeSleep", "0");
			String afterSleep = actionsEle.attributeValue("afterSleep", "0");
			String repeat = actionsEle.attributeValue("repeat", "1");
			
			List<SuiteAction> actionList = new ArrayList<SuiteAction>();
			
			SuitePage suitePage = new SuitePage(pageCls);
			suitePage.setActionList(actionList);
			suitePage.setRepeat(Integer.parseInt(repeat));
			
			pageList.add(suitePage);
			
			parse(actionList, actionsEle, beforeSleep, afterSleep);
		}
	}

	/**
	 * 解析具体的action元素
	 * @param actionList
	 * @param actionsEle action组元素
	 * @param afterSleep action所在组的休眠时间
	 * @param beforeSleep action所在组的休眠时间
	 */
	private void parse(List<SuiteAction> actionList, Element actionsEle,
			String beforeSleep, String afterSleep)
	{
		DefaultXPath xpath = new DefaultXPath("ns:action");
		xpath.setNamespaceContext(simpleNamespaceContext);
		
		List<Element> actionEleList = xpath.selectNodes(actionsEle);
		for(Element actionEle : actionEleList)
		{
			String field = actionEle.attributeValue("field");
			String name = actionEle.attributeValue("name", "click");
			String actionBeforeSleep = actionEle.attributeValue("beforeSleep", beforeSleep);
			String actionAfterSleep = actionEle.attributeValue("afterSleep", afterSleep);
			String repeat = actionEle.attributeValue("repeat", "1");
			
			SuiteAction suiteAction = new SuiteAction(field, name);
			suiteAction.setBeforeSleep(Long.parseLong(actionBeforeSleep));
			suiteAction.setAfterSleep(Long.parseLong(actionAfterSleep));
			suiteAction.setRepeat(Integer.parseInt(repeat));
			
			actionList.add(suiteAction);
		}
	}
}