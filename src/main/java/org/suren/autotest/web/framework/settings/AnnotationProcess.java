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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.suren.autotest.web.framework.annotation.AutoAttrLocator;
import org.suren.autotest.web.framework.annotation.AutoDataSource;
import org.suren.autotest.web.framework.annotation.AutoForm;
import org.suren.autotest.web.framework.annotation.AutoLocator;
import org.suren.autotest.web.framework.annotation.AutoLocators;
import org.suren.autotest.web.framework.annotation.AutoPage;
import org.suren.autotest.web.framework.annotation.AutoTextLocator;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.locator.SeleniumTextLocator;
import org.suren.autotest.web.framework.selenium.locator.SeleniumXAttrLocator;
import org.suren.autotest.web.framework.util.StringUtils;

import com.surenpi.autotest.webui.Form;
import com.surenpi.autotest.webui.Page;
import com.surenpi.autotest.webui.core.Locator;
import com.surenpi.autotest.webui.core.LocatorAware;
import com.surenpi.autotest.webui.core.LocatorType;
import com.surenpi.autotest.webui.core.StrategyType;
import com.surenpi.autotest.webui.ui.AbstractElement;

/**
 * @author suren
 * @date Aug 12, 2017 8:12:49 PM
 */
public class AnnotationProcess
{
    private static final Logger logger = LoggerFactory.getLogger(AnnotationProcess.class);
    
    private Map<String, DataSourceInfo> dataSourceMap = new HashMap<String, DataSourceInfo>();
    private ApplicationContext context;
    
    public AnnotationProcess(ApplicationContext context)
    {
        this.context = context;
    }
    
    public void scan(Map<String, Object> beanMap, Map<String, Page> pageMap,
            SeleniumEngine engine)
    {
        beanMap.forEach((name, bean) -> {
            if(!(bean instanceof Page))
            {
                return;
            }

            Page pageBean = (Page) bean;
            Class<?> beanCls = bean.getClass();
            if(Enhancer.isEnhanced(beanCls))
            {
                beanCls = beanCls.getSuperclass();
            }
            
            String clsName = beanCls.getName();
            pageMap.put(clsName, (Page) bean);

            //页面起始地址处理
            AutoPage autoPageAnno = beanCls.getAnnotation(AutoPage.class);
            String url = autoPageAnno.url();
            pageBean.setUrl(url);

            if(autoPageAnno.startPage())
            {
                //设置浏览器信息
                engine.setWidth(autoPageAnno.width());
                engine.setHeight(autoPageAnno.height());
                engine.setMaximize(autoPageAnno.maximize());
                engine.setDriverStr(autoPageAnno.browser());
                engine.setRemoteStr(autoPageAnno.remote());
            }

            //数据源处理
            autoDataSourceProcess(beanCls, pageBean);
            
            //属性上的注解处理
            fieldAnnotationProcess(pageBean);
        });
        
        Map<String, Form> formList = this.context.getBeansOfType(Form.class);
        formList.forEach((name, form) -> {
            Class<?> formClz = form.getClass();
            AutoForm autoForm = formClz.getAnnotation(AutoForm.class);
            
            List<Locator> locatorList = parseFormLocators(autoForm);

            Field[] fields = formClz.getDeclaredFields();
            if(!formClz.getSuperclass().equals(Form.class))
            {
                Field[] superClsFields = formClz.getSuperclass().getDeclaredFields();
                int oldSize = fields.length;
                int newSize = oldSize + superClsFields.length;
                
                fields = Arrays.copyOf(fields, newSize);
                for(int i = oldSize; i < newSize; i++)
                {
                    fields[i] = superClsFields[i - oldSize];
                }
            }
            
            formFieldAnnoProcess(form, fields, locatorList);
        });
    }

    private void formFieldAnnoProcess(Form form, Field[] fields, List<Locator> locatorList)
    {
        if(fields == null)
        {
            return;
        }
        
        for(Field field : fields)
        {
            AbstractElement element = null;
            
            field.setAccessible(true);
            try
            {
                Object fieldObj = field.get(form);
                if(!(fieldObj instanceof AbstractElement))
                {
                    continue;
                }
                
                element = (AbstractElement) fieldObj;
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                e.printStackTrace();
            }

            element.setStrategy(StrategyType.ZONE.getName());
            element.getLocatorList().addAll(locatorList);
            
            AutoLocator autoLocator = field.getAnnotation(AutoLocator.class);
            if(autoLocator != null)
            {
                LocatorType locatorType = autoLocator.locator();
                String value = autoLocator.value();
                long timeout = autoLocator.timeout();

                element.setParamPrefix("param");
                element.setTimeOut(timeout);
                switch (locatorType)
                {
                    case BY_ID:
                        element.setId(value);
                        break;
                    case BY_NAME:
                        element.setName(value);
                        break;
                    case BY_CSS:
                        element.setCSS(value);
                        break;
                    case BY_LINK_TEXT:
                        element.setLinkText(value);
                        break;
                    case BY_PARTIAL_LINK_TEXT:
                        element.setPartialLinkText(value);
                        break;
                    case BY_XPATH:
                        element.setXPath(value);
                        break;
                    case BY_TAGNAME:
                        element.setTagName(value);
                        break;
                    case BY_X_ATTR:
                    case BY_X_TEXT:
                    case BY_FRAME_NAME:
                    case BY_FRAME_INDEX:
                        logger.error("Not support locator by frame.");
                        break;
                }
            }
            
            AutoAttrLocator autoAttrLocator = field.getAnnotation(AutoAttrLocator.class);
            if(autoAttrLocator != null)
            {
                SeleniumXAttrLocator xAttrLocator = context.getBean(SeleniumXAttrLocator.class);
                xAttrLocator.setValue(autoAttrLocator.value());
                xAttrLocator.setExtend(autoAttrLocator.name());
                xAttrLocator.setTagName(autoAttrLocator.tagName());
                xAttrLocator.setCondition(autoAttrLocator.condition());
                xAttrLocator.setTimeout(autoAttrLocator.timeout());
                xAttrLocator.setOrder(autoAttrLocator.order());
                
                element.getLocatorList().add(xAttrLocator);
            }
            
            AutoTextLocator autoTextLocator = field.getAnnotation(AutoTextLocator.class);
            if(autoTextLocator != null)
            {
                SeleniumTextLocator textLocator = context.getBean(SeleniumTextLocator.class);
                textLocator.setCondition(autoTextLocator.condition());
                textLocator.setExtend(autoTextLocator.tagName());
                textLocator.setValue(autoTextLocator.text());
                textLocator.setTimeout(autoTextLocator.timeout());
                textLocator.setOrder(autoTextLocator.order());
                
                element.getLocatorList().add(textLocator);
            }
        }
    }

    private List<Locator> parseFormLocators(AutoForm autoForm)
    {
        List<Locator> locatorList = new ArrayList<Locator>();
        for(AutoLocator locator : autoForm.locators())
        {
            Map<String, Locator> beans = context.getBeansOfType(Locator.class);
            Collection<Locator> locatorCollect = beans.values();
            for(Locator locatorItem : locatorCollect)
            {
                if(!locator.locator().getName().equals(locatorItem.getType()))
                {
                    continue;
                }
                
                if(locatorItem instanceof LocatorAware)
                {
                    LocatorAware locatorAware = (LocatorAware) locatorItem;
                    locatorAware.setValue(locator.value());
                    locatorAware.setTimeout(locator.timeout());
                    locatorAware.setExtend(locator.extend());
                    locatorAware.setOrder(locator.order());
                    
                    locatorList.add(locatorItem);
                    
                    break;
                }
            }
        }
        
        for(AutoAttrLocator locator : autoForm.attrLocators())
        {
            SeleniumXAttrLocator seleniumXAttrLocator = context.getBean(SeleniumXAttrLocator.class);
            seleniumXAttrLocator.setTagName(locator.tagName());
            seleniumXAttrLocator.setExtend(locator.name());
            seleniumXAttrLocator.setValue(locator.value());
            seleniumXAttrLocator.setTimeout(locator.timeout());
            seleniumXAttrLocator.setOrder(locator.order());
            seleniumXAttrLocator.setCondition(locator.condition());
            
            locatorList.add(seleniumXAttrLocator);
        }
        
        for(AutoTextLocator locator : autoForm.textLocators())
        {
            SeleniumTextLocator seleniumTextLocator = context.getBean(SeleniumTextLocator.class);
            seleniumTextLocator.setExtend(locator.tagName());
            seleniumTextLocator.setValue(locator.text());
            seleniumTextLocator.setTimeout(locator.timeout());
            seleniumTextLocator.setOrder(locator.order());
            seleniumTextLocator.setCondition(locator.condition());
            
            locatorList.add(seleniumTextLocator);
        }
        
        return locatorList;
    }

    /**
     * 数据源处理
     * @param beanCls Page类的class类型
     * @param pageBean Page类对象
     */
    private void autoDataSourceProcess(Class<?> beanCls, Page pageBean)
    {
        AutoDataSource autoDataSource = beanCls.getAnnotation(AutoDataSource.class);
        if(autoDataSource != null)
        {
            String dsName = StringUtils.defaultIfBlank(autoDataSource.name(),
                    System.currentTimeMillis());
            pageBean.setDataSource(dsName);
            dataSourceMap.put(dsName,
                    new DataSourceInfo(autoDataSource.type(), autoDataSource.resource()));
        }
    }

    /**
     * 属性上的注解处理
     * @param bean Page类
    */
    private void fieldAnnotationProcess(Page bean)
    {
        //获取所有的属性
        Class<?> beanCls = bean.getClass();
        Field[] fields = beanCls.getDeclaredFields();
        if(!beanCls.getSuperclass().equals(Page.class))
        {
            Field[] superClsFields = beanCls.getSuperclass().getDeclaredFields();
            int oldSize = fields.length;
            int newSize = oldSize + superClsFields.length;
            
            fields = Arrays.copyOf(fields, newSize);
            for(int i = oldSize; i < newSize; i++)
            {
                fields[i] = superClsFields[i - oldSize];
            }
        }
        
        for(Field field : fields)
        {
            AbstractElement element = null;
            
            field.setAccessible(true);
            try
            {
                Object fieldObj = field.get(bean);
                if(!(fieldObj instanceof AbstractElement))
                {
                    continue;
                }
                
                element = (AbstractElement) fieldObj;
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
            
            AutoLocator autoLocator = field.getAnnotation(AutoLocator.class);
            if(autoLocator != null)
            {
                LocatorType locatorType = autoLocator.locator();
                String value = autoLocator.value();
                long timeout = autoLocator.timeout();

                element.setParamPrefix("param");
                element.setTimeOut(timeout);
                switch (locatorType)
                {
                    case BY_ID:
                        element.setId(value);
                        break;
                    case BY_NAME:
                        element.setName(value);
                        break;
                    case BY_CSS:
                        element.setCSS(value);
                        break;
                    case BY_LINK_TEXT:
                        element.setLinkText(value);
                        break;
                    case BY_PARTIAL_LINK_TEXT:
                        element.setPartialLinkText(value);
                        break;
                    case BY_XPATH:
                        element.setXPath(value);
                        break;
                    case BY_TAGNAME:
                        element.setTagName(value);
                        break;
                    case BY_X_ATTR:
                    case BY_X_TEXT:
                    case BY_FRAME_NAME:
                    case BY_FRAME_INDEX:
                        logger.error("Not support locator by frame.");
                        break;
                }
            }

            AutoAttrLocator autoAttrLocator = field.getAnnotation(AutoAttrLocator.class);
            if(autoAttrLocator != null)
            {
                SeleniumXAttrLocator xAttrLocator = context.getBean(SeleniumXAttrLocator.class);
                xAttrLocator.setValue(autoAttrLocator.value());
                xAttrLocator.setExtend(autoAttrLocator.name());
                xAttrLocator.setTagName(autoAttrLocator.tagName());
                xAttrLocator.setCondition(autoAttrLocator.condition());
                xAttrLocator.setTimeout(autoAttrLocator.timeout());
                
                element.getLocatorList().add(xAttrLocator);
            }
            
            AutoTextLocator autoTextLocator = field.getAnnotation(AutoTextLocator.class);
            if(autoTextLocator != null)
            {
                SeleniumTextLocator textLocator = context.getBean(SeleniumTextLocator.class);
                textLocator.setCondition(autoTextLocator.condition());
                textLocator.setExtend(autoTextLocator.tagName());
                textLocator.setValue(autoTextLocator.text());
                textLocator.setTimeout(autoTextLocator.timeout());
                
                element.getLocatorList().add(textLocator);
            }

            AutoLocators autoLocators = field.getAnnotation(AutoLocators.class);
            if(autoLocators != null)
            {
                element.setStrategy(autoLocators.strategy().getName());
                
                for(AutoLocator locator : autoLocators.locators())
                {
                    Map<String, Locator> beans = context.getBeansOfType(Locator.class);
                    Collection<Locator> locatorList = beans.values();
                    for(Locator locatorItem : locatorList)
                    {
                        if(!locator.locator().getName().equals(locatorItem.getType()))
                        {
                            continue;
                        }
                        
                        if(locatorItem instanceof LocatorAware)
                        {
                            LocatorAware locatorAware = (LocatorAware) locatorItem;
                            locatorAware.setValue(locator.value());
                            locatorAware.setTimeout(locator.timeout());
                            locatorAware.setExtend(locator.extend());
                            locatorAware.setOrder(locator.order());
                            
                            element.getLocatorList().add(locatorItem);
                            
                            break;
                        }
                    }
                }
                
                for(AutoAttrLocator locator : autoLocators.attrLocators())
                {
                    SeleniumXAttrLocator seleniumXAttrLocator = context.getBean(SeleniumXAttrLocator.class);
                    seleniumXAttrLocator.setTagName(locator.tagName());
                    seleniumXAttrLocator.setExtend(locator.name());
                    seleniumXAttrLocator.setValue(locator.value());
                    seleniumXAttrLocator.setTimeout(locator.timeout());
                    seleniumXAttrLocator.setOrder(locator.order());
                    seleniumXAttrLocator.setCondition(locator.condition());
                    
                    element.getLocatorList().add(seleniumXAttrLocator);
                }
                
                for(AutoTextLocator locator : autoLocators.textLocators())
                {
                    SeleniumTextLocator seleniumTextLocator = context.getBean(SeleniumTextLocator.class);
                    seleniumTextLocator.setExtend(locator.tagName());
                    seleniumTextLocator.setValue(locator.text());
                    seleniumTextLocator.setTimeout(locator.timeout());
                    seleniumTextLocator.setOrder(locator.order());
                    seleniumTextLocator.setCondition(locator.condition());
                    
                    element.getLocatorList().add(seleniumTextLocator);
                }
            }
        }
    }
}
