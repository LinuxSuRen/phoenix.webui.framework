/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.suren.autotest.web.framework.core.LocatorAware;

/**
 * @author suren
 * @date Jul 30, 2016 7:47:18 PM
 */
public abstract class AbstractTreeLocator extends AbstractLocator<WebElement>
	implements ApplicationContextAware
{
	private String hostType;
	private String hostValue;
	
	private ApplicationContext context;

	@SuppressWarnings("rawtypes")
	@Override
	protected By getBy()
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
