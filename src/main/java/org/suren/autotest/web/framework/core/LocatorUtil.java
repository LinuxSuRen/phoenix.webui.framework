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

package org.suren.autotest.web.framework.core;

import java.util.Map;

import org.suren.autotest.web.framework.core.ui.AbstractElement;

/**
 * 定位信息设置工具类
 * @author suren
 * @date 2017年1月16日 下午2:04:46
 */
public class LocatorUtil
{
	/**
	 * 把定位信息集合设置到元素抽象类中
	 * @param locatorMap
	 * @param absEle
	 */
	public static void setLocator(Map<String, String> locatorMap, AbstractElement absEle)
	{
		for(String key : locatorMap.keySet())
		{
			String value = locatorMap.get(key);
					
			switch(key)
			{
				case "byId":
					absEle.setId(value);
					break;
				case "byName":
					absEle.setName(value);
					break;
				case "byCss":
					absEle.setCSS(value);
					break;
				case "byXpath":
					absEle.setXPath(value);
					break;
				case "byLinkText":
					absEle.setLinkText(value);
					break;
				case "byPartialLinkText":
					absEle.setPartialLinkText(value);
					break;
				case "strategy":
					absEle.setStrategy(value);
					break;
			}
		}
	}
}
