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
