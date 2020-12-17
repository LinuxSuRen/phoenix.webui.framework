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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.surenpi.autotest.webui.core.Locator;

/**
 * 元素文本定位器
 * @author linuxsuren
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeleniumTextLocator extends AbstractLocator<WebElement>
{
    private String tagName;
    private String text;
    private int condition = Locator.EQUAL;

	@Override
	public String getType()
	{
		return "byText";
	}

    @Override
    public void setValue(String value)
    {
        this.text = value;
    }

    @Override
    public void setExtend(String extend)
    {
        this.tagName = extend;
    }

    public void setCondition(int condition)
    {
        this.condition = condition;
    }

    @Override
    public By getBy()
    {
        By by = null;
        if(condition == Locator.EQUAL)
        {
            by = By.xpath(String.format("//%s[text()='%s']", this.tagName, this.text));
        }
        else if(condition == Locator.LIKE)
        {
            by = By.xpath(String.format("//%s[contains(text(),'%s')]", this.tagName, this.text));
        }
        
        return by;
    }

}
