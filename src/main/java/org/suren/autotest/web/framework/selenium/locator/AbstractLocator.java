/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.suren.autotest.web.framework.core.Locator;
import org.suren.autotest.web.framework.core.LocatorAware;
import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author suren
 * @date 2016年7月25日 下午12:43:15
 */
public abstract class AbstractLocator<E> implements Locator, LocatorAware
{
	private String value;
	private long timeout;

	@Override
	public String getValue()
	{
		return value;
	}

	@Override
	public void setValue(String value)
	{
		this.value = value;
	}

	@Override
	public long getTimeout()
	{
		return timeout;
	}

	@Override
	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
	
	public E findElement(WebDriver driver)
	{
		By by = getBy();
		
		elementWait(driver, getTimeout(), by);
		
		return (E) driver.findElement(by);
	}
	
	public List<E> findElements()
	{
		return null;
	}
	
	protected abstract By getBy();

	/**
	 * 根据超时时间来等待元素
	 * @param locator
	 * @param by
	 */
	@SuppressWarnings("unchecked")
	protected void elementWait(WebDriver driver, long timeout, By by)
	{
		eleWait(driver, timeout, ExpectedConditions.visibilityOfElementLocated(by));
	}

	@SuppressWarnings("unchecked")
	protected void iframeWait(WebDriver driver, long timeout, int index)
	{
		eleWait(driver, timeout, ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
	}
	
	@SuppressWarnings("unchecked")
	protected void iframeWait(WebDriver driver, long timeout, String locator)
	{
		eleWait(driver, timeout, ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
	}
	
	@SuppressWarnings("unchecked")
	protected void eleWait(WebDriver driver, long timeout, ExpectedCondition<? extends SearchContext> ...isTrueArray)
	{
		if(timeout > 0 && isTrueArray != null && isTrueArray.length > 0)
		{
//			logger.debug(String.format("prepare to waiting [%s] seconds.", timeout));
			
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			for(ExpectedCondition<? extends SearchContext> isTrue : isTrueArray)
			{
				wait.until(isTrue);
			}
			
//			logger.debug(String.format("prepare to waiting [%s] seconds done.", timeout));
		}
	}
}
