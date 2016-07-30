/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.locator;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author suren
 * @date 2016年7月25日 上午8:12:32
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumCssLocator extends AbstractLocator<WebElement>
{
	@Override
	public String getType()
	{
		return "byCss";
	}

	@Override
	protected By getBy()
	{
		return By.className(getValue());
	}

	@Override
	public WebElement findElement(SearchContext driver)
	{
		String css = getValue();
		String[] cssArray = css.split(" ");
		setValue(cssArray[0]);
		
		By by = getBy();
		setValue(css);
		List<WebElement> elementList = driver.findElements(by);
		for(WebElement ele : elementList)
		{
			if(driver instanceof WebDriver)
			{
				new Actions((WebDriver) driver).moveToElement(ele);
			}
			
			if(css.equals(ele.getAttribute("class")))
			{
				return ele;
			}
		}
		
		return null;
	}
}
