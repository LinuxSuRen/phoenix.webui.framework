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
 * css定位器
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
		final String priorityCss; //优先定位的css值
		final String targetCss;
		final String css = getValue();
		
		String[] priorityCssArray = css.split(",");
		if(priorityCssArray.length >= 2)
		{
			targetCss = priorityCssArray[1];
			priorityCss = priorityCssArray[0];
		}
		else
		{
			targetCss = css;
			priorityCss = css.split(" ")[0];
		}
		
		//设定当前的定位器
		setValue(priorityCss);
		By by = getBy();
		
		//值还原
		setValue(css);
		List<WebElement> elementList = driver.findElements(by);
		for(WebElement ele : elementList)
		{
			if(driver instanceof WebDriver)
			{
				new Actions((WebDriver) driver).moveToElement(ele);
			}
			
			if(targetCss.equals(ele.getAttribute("class")))
			{
				return ele;
			}
		}
		
		return null;
	}
}
