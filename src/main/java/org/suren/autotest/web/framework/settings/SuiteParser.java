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

package org.suren.autotest.web.framework.settings;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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
import org.suren.autotest.web.framework.util.StringUtils;

/**
 * 测试套件配置文件解析
 * @author suren
 * @date 2016年9月7日 下午9:36:16
 */
public class SuiteParser
{
	/** 命名空间地址 */
	public static final String NS_URI = "http://suite.surenpi.com";
	
	/** 支持的文件后缀 */
	private final List<String> supportList = new ArrayList<String>(1);
	private SimpleNamespaceContext simpleNamespaceContext = new SimpleNamespaceContext();
	
	/**
	 * 解析测试套件配置文件
	 * @param suiteInputStream 配置文件输入流
	 * @return 测试套件对象
	 * @throws DocumentException
	 */
	public Suite parse(InputStream suiteInputStream) throws DocumentException
	{
		SAXReader reader = new SAXReader();
		reader.setEncoding("utf-8");
		
		Document document = reader.read(suiteInputStream);
		
		simpleNamespaceContext.addNamespace("ns", NS_URI);
		
		XPath xpath = new DefaultXPath("/ns:suite");
		xpath.setNamespaceContext(simpleNamespaceContext);
		Element suiteEle = (Element) xpath.selectSingleNode(document);
		if (suiteEle == null)
		{
			throw new RuntimeException("Can not found suite config.");
		}
		
		Suite suite = new Suite();
		String xmlConfPath = suiteEle.attributeValue("pageConfig");
		String pagePackage = suiteEle.attributeValue("pagePackage", "");
		String rows = suiteEle.attributeValue("rows", "1");
		String lackLines = suiteEle.attributeValue("lackLines", "nearby");
		String errorLines = suiteEle.attributeValue("errorLines", "stop");
		String afterSleep = suiteEle.attributeValue("afterSleep", "0");
		
		suite.setXmlConfPath(xmlConfPath);
		suite.setPagePackage(pagePackage);
		suite.setRows(rows);
		suite.setLackLines(lackLines);
		suite.setErrorLines(errorLines);
		suite.setAfterSleep(Long.parseLong(afterSleep));
		
		pagesParse(document, suite);
		
		return suite;
	}
	
	/**
	 * 返回结果不允许修改
	 * @return 支持的文件后缀
	 */
	public List<String> getSupport()
	{
		if(supportList.size() == 0)
		{
			supportList.add(".xml");
		}
		
		return Collections.unmodifiableList(supportList);
	}

	/**
	 * 解析page配置
	 * @param document
	 * @param suite
	 */
	private void pagesParse(Document document, Suite suite)
	{
		List<SuitePage> pageList = new ArrayList<SuitePage>();
		suite.setPageList(pageList);
		
		String pagePackage = suite.getPagePackage();
		if(!StringUtils.isBlank(pagePackage))
		{
			pagePackage = (pagePackage.trim() + ".");
		}
		
		XPath xpath = new DefaultXPath("/ns:suite/ns:page");
		xpath.setNamespaceContext(simpleNamespaceContext);
		
		List<Element> pageNodes = xpath.selectNodes(document);
		if(pageNodes == null || pageNodes.size() == 0)
		{
			throw new RuntimeException("Can not found page config.");
		}
		
		for(Element pageEle : pageNodes)
		{
			String pageCls = pageEle.attributeValue("class");
			
			xpath = new DefaultXPath("ns:actions");
			xpath.setNamespaceContext(simpleNamespaceContext);
			
			Element actionsEle = (Element) xpath.selectSingleNode(pageEle);
			if(actionsEle == null)
			{
				throw new RuntimeException("Can not found actions config.");
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
			
			SuitePage suitePage = new SuitePage(String.format("%s%s", pagePackage, pageCls));
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
	private void parse(List<SuiteAction> actionList, final Element actionsEle,
			String beforeSleep, String afterSleep)
	{
		DefaultXPath xpath = new DefaultXPath("ns:action");
		xpath.setNamespaceContext(simpleNamespaceContext);
		
		List<Element> actionEleList = xpath.selectNodes(actionsEle);
		for(Element actionEle : actionEleList)
		{
			String field = actionEle.attributeValue("field");
			String name = actionEle.attributeValue("name", "click");
			String invoker = actionEle.attributeValue("invoker");
			String actionBeforeSleep = actionEle.attributeValue("beforeSleep", beforeSleep);
			String actionAfterSleep = actionEle.attributeValue("afterSleep", afterSleep);
			String repeat = actionEle.attributeValue("repeat", "1");
			String disable = actionEle.attributeValue("disable", "false");
			
			if(Boolean.parseBoolean(disable))
			{
				continue;
			}
			
			SuiteAction suiteAction = new SuiteAction(field, name);
			suiteAction.setInvoker(invoker);
			suiteAction.setBeforeSleep(Long.parseLong(actionBeforeSleep));
			suiteAction.setAfterSleep(Long.parseLong(actionAfterSleep));
			suiteAction.setRepeat(Integer.parseInt(repeat));
			
			parseActionParam(actionEle, suiteAction);
			
			actionList.add(suiteAction);
		}
	}

	/**
	 * @param actionEle
	 * @param suiteAction
	 */
	private void parseActionParam(Element actionEle, SuiteAction suiteAction)
	{
		List<String> paramList = suiteAction.getParamList();
		
		List<Element> paramEleList = actionEle.elements("param");
		if(paramEleList != null)
		{
			for(Element paramEle : paramEleList)
			{
				paramList.add(paramEle.attributeValue("value"));
			}
		}
	}
}