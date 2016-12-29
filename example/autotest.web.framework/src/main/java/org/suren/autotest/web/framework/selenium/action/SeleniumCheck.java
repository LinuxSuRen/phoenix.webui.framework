/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.action;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.Locator;
import org.suren.autotest.web.framework.core.action.CheckAble;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.locator.SeleniumValueLocator;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

/**
 * 复选框选择
 * @author suren
 * @date Jul 27, 2016 1:00:30 PM
 */
@Component
public class SeleniumCheck implements CheckAble, ApplicationContextAware
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumCheck.class);
	
	@Autowired
	private SearchStrategyUtils		searchStrategyUtils;
	
	private ApplicationContext context;

	@Override
	public void checkByText(Element element, String text)
	{
		checkByValue(element, text);
	}

	@Override
	public void checkByValue(Element element, String value)
	{
		WebElement webEle = searchStrategyUtils.findStrategy(WebElement.class, element).search(element);
		if(webEle == null)
		{
			logger.error(String.format("can not found element byText [%s].", value));
			return;
		}
		
		List<Locator> locatorList = element.getLocatorList();
		List<Locator> tmpList = new ArrayList<Locator>(locatorList);
		
		ElementSearchStrategy<WebElement> strategy = context.getBean("zoneSearchStrategy", ElementSearchStrategy.class);
		
		SeleniumValueLocator valueLocator = context.getBean(SeleniumValueLocator.class);
		valueLocator.setHostType("byTagName");
		valueLocator.setHostValue("input");
		
		locatorList.clear();
		locatorList.add(valueLocator);
		valueLocator.setValue(value);
		
		WebElement itemWebEle = null;
		try
		{
			itemWebEle = strategy.search(element);
			if(itemWebEle != null)
			{
				if(!itemWebEle.isSelected())
				{
					itemWebEle.click();
				}
			}
		}
		catch(ElementNotVisibleException e)
		{
			e.printStackTrace();
			logger.error(String.format("Element [%s] click error, parent [%s], text [%s].",
					itemWebEle, element, value));
		}
		finally
		{
			locatorList.clear();
			locatorList.addAll(tmpList);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException
	{
		this.context = applicationContext;
	}

}
