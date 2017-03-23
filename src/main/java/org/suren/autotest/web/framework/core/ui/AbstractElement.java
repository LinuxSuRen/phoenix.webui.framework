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

package org.suren.autotest.web.framework.core.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.suren.autotest.web.framework.core.Locator;
import org.suren.autotest.web.framework.util.StringUtils;

/**
 * 所有HTML页面元素的抽象， 包含了元素的id、name、tagName、css、xpath、linktext、partialLinkText等属性
 * 
 * @author suren
 * @since jdk1.6 2016年6月30日
 */
public abstract class AbstractElement implements Element
{
	private String							id;
	private String							name;
	private String							tagName;
	private String							CSS;
	private String							XPath;
	private String							linkText;
	private String							partialLinkText;
	private List<Locator>					locatorList = new ArrayList<Locator>();
	private String							strategy;
	/** 用于保存元素对象相关的数据 */
	private Map<String, Object>				data = new HashMap<String, Object>();
	private String paramPrefix;
	private long timeOut;

	@Override
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	@Override
	public String getCSS()
	{
		return CSS;
	}

	public void setCSS(String cSS)
	{
		CSS = cSS;
	}

	@Override
	public String getXPath()
	{
		return XPath;
	}

	public void setXPath(String xPath)
	{
		XPath = xPath;
	}

	@Override
	public String getLinkText()
	{
		return linkText;
	}

	public void setLinkText(String linkText)
	{
		this.linkText = linkText;
	}

	@Override
	public String getPartialLinkText()
	{
		return partialLinkText;
	}

	public void setPartialLinkText(String partialLinkText)
	{
		this.partialLinkText = partialLinkText;
	}

	/**
	 * @return the locatorList
	 */
	@Override
	public List<Locator> getLocatorList()
	{
		return locatorList;
	}

	/**
	 * @param locatorList the locatorList to set
	 */
	public void setLocatorList(List<Locator> locatorList)
	{
		this.locatorList = locatorList;
	}

	@Override
	public String getStrategy()
	{
		return strategy;
	}

	public void setStrategy(String strategy)
	{
		this.strategy = strategy;
	}
	
	/**
	 * 添加数据
	 * @param key
	 * @param value
	 */
	public void putData(String key, Object value)
	{
		data.put(key, value);
	}
	
	/**
	 * 移出数据
	 * @param key
	 */
	public void removeData(String key)
	{
		data.remove(key);
	}
	
	/**
	 * @param key
	 * @return 是否包含指定key的数据
	 */
	public boolean containsKey(String key)
	{
		return data.containsKey(key);
	}
	
	/**
	 * 清空数据
	 */
	public void clearData()
	{
		data.clear();
	}
	
	/**
	 * 获取数据
	 * @param key
	 * @return
	 */
	public Object getData(String key)
	{
		return data.get(key);
	}
	
	/**
	 * 获取数据的字符串
	 * @param key
	 * @return
	 */
	public String getDataStr(String key)
	{
		Object dataObj = data.get(key);
		if(dataObj == null)
		{
			return "";
		}
		else
		{
			return dataObj.toString();
		}
	}

	/**
	 * @return 可用返回true，否则返回false
	 */
	public abstract boolean isEnabled();

	/**
	 * @return 隐藏返回true，否则返回false
	 */
	public abstract boolean isHidden();
	
	/**
	 * 把参数型的值进行转换
	 * @param value
	 * @return
	 */
	public String paramTranslate(String value)
	{
		String result = value;
		result = StringUtils.paramTranslate(data, getParamPrefix(), result);
		
		return result;
	}

	public String getParamPrefix()
	{
		return paramPrefix;
	}

	public void setParamPrefix(String paramPrefix)
	{
		this.paramPrefix = paramPrefix;
	}

	/**
	 * @return the timeOut
	 */
	public long getTimeOut()
	{
		return timeOut;
	}

	/**
	 * @param timeOut the timeOut to set
	 */
	public void setTimeOut(long timeOut)
	{
		this.timeOut = timeOut;
	}
}
