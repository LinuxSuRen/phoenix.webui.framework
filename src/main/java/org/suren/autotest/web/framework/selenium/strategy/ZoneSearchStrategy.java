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

package org.suren.autotest.web.framework.selenium.strategy;

import java.util.Comparator;
import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.locator.AbstractLocator;

import com.surenpi.autotest.utils.ThreadUtil;
import com.surenpi.autotest.webui.core.ElementSearchStrategy;
import com.surenpi.autotest.webui.core.Locator;
import com.surenpi.autotest.webui.ui.AbstractElement;
import com.surenpi.autotest.webui.ui.Element;

/**
 * 区域定位元素查找策略
 * 
 * @see PrioritySearchStrategy
 * @see CyleSearchStrategy
 * @author <a href="http://surenpi.com">suren</a>
 */
@Component("zoneSearchStrategy")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZoneSearchStrategy implements ElementSearchStrategy<WebElement>, ParentElement
{
	private static final Logger logger = LoggerFactory.getLogger(ZoneSearchStrategy.class);
	
	@Autowired
	private SeleniumEngine engine;
	
	private int failedCount = 0;
	private int maxFailed = 6;
	private int timeout = 1500;
	
	private WebElement parentWebElement;

	@SuppressWarnings("unchecked")
	@Override
	public WebElement search(Element element)
	{
		List<Locator> locators = element.getLocatorList();
		if(locators == null)
		{
			return null;
		}
		
		logger.info(String.format("zone search strategy, locators count[%s].", locators.size()));
		
		AbstractElement absEle = null;
		if(element instanceof AbstractElement)
		{
			absEle = (AbstractElement) element;
		}
		
		WebDriver driver = engine.getDriver();
		locators.sort(new Comparator<Locator>(){

            @Override
            public int compare(Locator o1, Locator o2)
            {
                return o1.getOrder() - o2.getOrder();
            }
        });
		
		for(Locator locator : locators)
		{
			if(!(locator instanceof AbstractLocator<?>))
			{
				logger.warn(String.format("Unknow locator type, not subclass of AbstractLocator, [%s].",
						locator.getClass()));
				continue;
			}
			
			AbstractLocator<WebElement> absLocator = ((AbstractLocator<WebElement>) locator);
			if(absEle != null)
			{
				//动态参数转换
				absLocator.setValue(absEle.paramTranslate(absLocator.getValue()));
			}

			if(parentWebElement != null)
			{
				parentWebElement = retry(absLocator, parentWebElement); 
			}
			else
			{
				parentWebElement = retry(absLocator, driver);
			}
		}
		
		return parentWebElement;
	}
	
	/**
	 * 失败重试
	 * @param absLocator
	 * @param webEle
	 * @return
	 */
	private WebElement retry(AbstractLocator<WebElement> absLocator, SearchContext webEle)
	{
		WebElement result = null;
		
		if(webEle != null)
		{
			result = absLocator.findElement(webEle);
		}
		else
		{
			result = absLocator.findElement(webEle);
		}
		
		if(result != null || ++failedCount > maxFailed)
		{
			return result;
		}
		else
		{
			logger.warn("Can not found element by locator {}, "
					+ "will retry locate again {} millis later, failed times {}.",
					absLocator, timeout, failedCount);
			ThreadUtil.silentSleep(timeout);
			return retry(absLocator, webEle);
		}
	}

	@Override
	public void setParent(WebElement parentWebElement) {
		this.parentWebElement = parentWebElement;
	}

	@Override
	public String description()
	{
		return String.format("区域搜索策略，按照元素的区域划分，分层次地进行多次定位搜索。每次元素查找失败，"
				+ "都会进行重试（%s次），每次失败后的超时时间为%s毫秒。", maxFailed, timeout);
	}
}
