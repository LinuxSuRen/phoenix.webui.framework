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

package org.suren.autotest.web.framework.settings;

import java.util.Collection;
import java.util.Map;

import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.suren.autotest.web.framework.util.StringUtils;

import com.surenpi.autotest.webui.core.Locator;
import com.surenpi.autotest.webui.core.LocatorAware;
import com.surenpi.autotest.webui.ui.AbstractElement;

/**
 * 元素定位器信息解析
 * @author suren
 * @since 2016年7月28日 上午8:18:01
 */
class FieldLocatorsVisitor extends VisitorSupport
{
    private static final Logger logger = LoggerFactory.getLogger(FieldLocatorsVisitor.class);
    
    private AbstractElement absEle;
    private ApplicationContext context;
    
    public FieldLocatorsVisitor(AbstractElement element, ApplicationContext context)
    {
        this.absEle = element;
        this.context = context;
    }
    
    @Override
    public void visit(Element node)
    {
        if (!"locator".equals(node.getName()))
        {
            return;
        }
        
        String name = node.attributeValue("name");
        String value = node.attributeValue("value");
        String timeoutStr = node.attributeValue("timeout");
        String extend = node.attributeValue("extend");
        
        if(StringUtils.isBlank(name) || StringUtils.isBlank(value))
        {
            logger.error("locator has empty name or value.");
        }
        
        long timeout = 0;
        if(StringUtils.isNotBlank(timeoutStr))
        {
            try
            {
                timeout = Long.parseLong(timeoutStr);
            }
            catch(NumberFormatException e){}
        }
        
        Map<String, Locator> beans = context.getBeansOfType(Locator.class);
        Collection<Locator> locatorList = beans.values();
        for(Locator locator : locatorList)
        {
            if(!name.equals(locator.getType()))
            {
                continue;
            }
            
            if(locator instanceof LocatorAware)
            {
                LocatorAware locatorAware = (LocatorAware) locator;
                locatorAware.setValue(value);
                locatorAware.setTimeout(timeout);
                locatorAware.setExtend(extend);
                
                absEle.getLocatorList().add(locator);
                
                break;
            }
        }
    }
}