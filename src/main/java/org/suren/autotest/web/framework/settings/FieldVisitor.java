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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;

import com.surenpi.autotest.webui.ui.AbstractElement;
import com.surenpi.autotest.webui.ui.Text;

/**
 * @author suren
 * @date Aug 13, 2017 9:20:41 AM
 */
class FieldVisitor extends VisitorSupport
{
    private static final Logger logger = LoggerFactory.getLogger(FieldVisitor.class);
    
    private Object pageInstance;
    private String pageClsStr;
    private ApplicationContext context;
    private Class<?> pageCls;

    public FieldVisitor(Object pageInstance, String pageClsStr,
            ApplicationContext context)
    {
        this.pageInstance = pageInstance;
        this.pageClsStr = pageClsStr;
        this.context = context;
        this.pageCls = pageInstance.getClass();
    }

    @Override
    public void visit(Element node)
    {
        if (!"field".equals(node.getName()))
        {
            return;
        }

        String fieldName = node.attributeValue("name");
        String byId = node.attributeValue("byId");
        String byCss = node.attributeValue("byCss");
        String byName = node.attributeValue("byName");
        String byXpath = node.attributeValue("byXpath");
        String byLinkText = node.attributeValue("byLinkText");
        String byPartialLinkText = node.attributeValue("byPartialLinkText");
        String byTagName = node.attributeValue("byTagName");
        String data = node.attributeValue("data");
        String type = node.attributeValue("type");
        String strategy = node.attributeValue("strategy");
        String paramPrefix = node.attributeValue("paramPrefix", "param");
        String timeOut = node.attributeValue("timeout", "0");
        if (fieldName == null || "".equals(fieldName))
        {
            return;
        }

        AbstractElement ele = null;
        try
        {
            Method getterMethod = BeanUtils.findMethod(pageCls,
                    "get" + fieldName.substring(0, 1).toUpperCase()
                            + fieldName.substring(1));
            if (getterMethod != null)
            {
                ele = (AbstractElement) getterMethod
                        .invoke(pageInstance);

                if (ele == null)
                {
                    logger.error(String.format(
                            "element [%s] is null, maybe you not set autowired.",
                            fieldName));
                    return;
                }

                if ("input".equals(type))
                {
                    Text text = (Text) ele;
                    text.setValue(data);

                    ele = text;
                }

                ele.setId(byId);
                ele.setName(byName);
                ele.setTagName(byTagName);
                ele.setXPath(byXpath);
                ele.setCSS(byCss);
                ele.setLinkText(byLinkText);
                ele.setPartialLinkText(byPartialLinkText);
                ele.setStrategy(strategy);
                ele.setParamPrefix(paramPrefix);
                ele.setTimeOut(Long.parseLong(timeOut));
            }
            else
            {
                logger.error(String.format("page cls [%s], field [%s]",
                                pageClsStr, fieldName));
            }
        }
        catch (IllegalAccessException e)
        {
            logger.error(e.getMessage(), e);
        }
        catch (IllegalArgumentException e)
        {
            logger.error(e.getMessage(), e);
        }
        catch (InvocationTargetException e)
        {
            logger.error(e.getMessage(), e);
        }
        catch (ClassCastException e)
        {
            logger.error(String.format("fieldName [%s]", fieldName), e);
        }

        node.accept(new FieldLocatorsVisitor(ele, context));
    }
}
