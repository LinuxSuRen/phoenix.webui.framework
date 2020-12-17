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

package org.suren.autotest.web.framework.selenium.locator;

import java.util.Collection;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.surenpi.autotest.utils.StringUtils;
import com.surenpi.autotest.webui.core.LocatorAware;

/**
 * 通过属性结构来定位元素的抽象父类
 * @author linuxsuren
 */
public abstract class AbstractTreeLocator extends AbstractLocator<WebElement>
	implements ApplicationContextAware
{
	private String hostType;
	private String hostValue;
	
	private ApplicationContext context;

	@SuppressWarnings("rawtypes")
	@Override
	public By getBy()
	{
		if(StringUtils.isAnyBlank(getHostType(), getHostValue()))
		{
			throw new IllegalArgumentException("HostType or HostValue is required in AbstractTreeLocator.");
		}
		
		Map<String, AbstractLocator> beans = context.getBeansOfType(AbstractLocator.class);
		Collection<AbstractLocator> locators = beans.values();
		for(AbstractLocator locator : locators)
		{
			LocatorAware locatorWare = null;
			if(!(locator instanceof LocatorAware))
			{
				continue;
			}
			
			if(getHostType().equals(locator.getType()))
			{
				locatorWare = (LocatorAware) locator;
				
				String oldValue = locator.getValue();
				locatorWare.setValue(getHostValue());
				
				By by = locator.getBy();
				
				locatorWare.setValue(oldValue);
				
				return by;
			}
		}
		
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException
	{
		this.context = context;
	}
	
	public String getHostType()
	{
		return hostType;
	}
	
	public void setHostType(String hostType)
	{
		this.hostType = hostType;
	}
	
	public String getHostValue()
	{
		return hostValue;
	}
	
	public void setHostValue(String hostValue)
	{
		this.hostValue = hostValue;
	}
}
