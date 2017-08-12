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

package org.suren.autotest.web.framework.selenium.action.multi;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.strategy.SearchStrategyUtils;

import com.surenpi.autotest.webui.action.MultiValueEditor;
import com.surenpi.autotest.webui.core.ElementFilter;
import com.surenpi.autotest.webui.core.ElementsSearchStrategy;
import com.surenpi.autotest.webui.ui.Element;

/**
 * @author suren
 * @since 2017年8月11日 下午5:28:06
 */
@Component
public class SeleniumMultiValueEditor implements MultiValueEditor
{
    @Autowired
    private SearchStrategyUtils     searchStrategyUtils;
    private ElementFilter filter;

    @Override
    public void setValue(Element ele, Object value)
    {
        ElementsSearchStrategy<WebElement> strategy = searchStrategyUtils.findElementsStrategy(WebElement.class, ele);
        
        List<WebElement> eleList = strategy.searchAll(ele);
        
        for(int i = 0; i < eleList.size(); i++)
        {
            WebElement webEle = eleList.get(i);
            String tagName = webEle.getTagName();
            String text = webEle.getText();
            
            String attrName = null;
            String attrValue = null;
            
            if(!webEle.isDisplayed())
            {
                continue;
            }
            
            if(filter.filter(tagName, attrName, attrValue, text))
            {
                webEle.sendKeys(value.toString());
            }
        }
    }

    @Override
    public MultiValueEditor setFilter(ElementFilter filter)
    {
        this.filter = filter;
        
        return this;
    }
}
