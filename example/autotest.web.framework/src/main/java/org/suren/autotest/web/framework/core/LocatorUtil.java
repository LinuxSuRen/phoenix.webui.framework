/**
 * http://surenpi.com
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
