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

package org.suren.autotest.web.framework.selenium;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;

/**
 * @author suren
 * @date 2017年2月20日 下午3:52:07
 */
public class DriverMapping
{
	private Document document;

	public void init()
	{
		try(InputStream input = this.getClass().getClassLoader().getResourceAsStream("driver.mapping.xml");
				InputStream test = this.getClass().getClassLoader().getResourceAsStream("engine.properties"))
		{
			SAXReader reader = new SAXReader();
			
			document = reader.read(input);
			
			Properties pro = new Properties();
			pro.load(test);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
	}
	
	public String getUrl(String browser, String ver)
	{
		return getUrl(browser, ver, "win32", "32");
	}
	
	public String getUrl(String browser, String ver, String os, String arch)
	{
		String xpathStr = String.format("//drivers/driver[@type='%s']/supports/browser[@version='%s']",
				browser, ver);
		XPath xpath = new DefaultXPath(xpathStr);

		String path = null;
		List<Element> nodes = xpath.selectNodes(document);
		for(Element ele : nodes)
		{
			List<Element> itemList = ele.getParent().getParent().element("items").elements("item");
			for(Element item : itemList)
			{
				if(os.equals(item.attributeValue("os")) && arch.equals(item.attributeValue("arch")))
				{
					path = item.attributeValue("path");
					break;
				}
			}
		}
		
		if(path != null)
		{
			String base = document.getRootElement().attributeValue("base");
			path = base + path;
		}
		
		return path;
	}
	
	public static void main(String[] args)
	{
		DriverMapping driverMapping = new DriverMapping();
		driverMapping.init();
		
		String url = driverMapping.getUrl("chrome", "56");
		
		System.out.println(url);
		
		System.out.println(System.getProperty("os.arch"));
	}
}
