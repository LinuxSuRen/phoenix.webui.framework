/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * @author suren
 * @date 2016年7月29日 下午2:54:39
 */
public abstract class SeleniumAttrLocator extends AbstractLocator<WebElement>
{
	@Override
	public WebElement findElement(WebDriver driver)
	{
		String attrName = getAttrName();
		String attrVal = getValue();
		By by = getBy();

		List<WebElement> elementList = driver.findElements(by);
		for(WebElement ele : elementList)
		{
			new Actions(driver).moveToElement(ele);
			if(attrVal.equals(ele.getAttribute(attrName)))
			{
				return ele;
			}
		}
		
		return null;
	}
	
	public abstract String getAttrName();
}
