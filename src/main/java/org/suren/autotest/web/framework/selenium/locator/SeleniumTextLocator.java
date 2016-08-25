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
 * 元素文本定位器
 * @author suren
 * @date 2016年7月27日 下午12:11:23
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumTextLocator extends AbstractTreeLocator
{

	@Override
	public String getType()
	{
		return "byText";
	}

	@Override
	public WebElement findElement(SearchContext driver)
	{
		String text = getValue();
		
		By by = getBy();

		List<WebElement> elementList = driver.findElements(by);
		for(WebElement ele : elementList)
		{
			if(driver instanceof WebDriver)
			{
				new Actions((WebDriver) driver).moveToElement(ele);
			}
			
			if(text.equals(ele.getText()))
			{
				return ele;
			}
		}
		
		return null;
	}

}
