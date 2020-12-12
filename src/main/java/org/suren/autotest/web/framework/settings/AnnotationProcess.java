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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.suren.autotest.web.framework.annotation.*;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.selenium.locator.SeleniumTextLocator;
import org.suren.autotest.web.framework.selenium.locator.SeleniumXAttrLocator;

import com.surenpi.autotest.utils.StringUtils;
import com.surenpi.autotest.webui.Form;
import com.surenpi.autotest.webui.Page;
import com.surenpi.autotest.webui.core.Locator;
import com.surenpi.autotest.webui.core.LocatorAware;
import com.surenpi.autotest.webui.core.LocatorType;
import com.surenpi.autotest.webui.core.StrategyType;
import com.surenpi.autotest.webui.ui.AbstractElement;

/**
 * 注解处理类
 * @author <a href="http://surenpi.com">suren</a>
 * @since 2.1.0
 */
public class AnnotationProcess
{
    private static final Logger logger = LoggerFactory.getLogger(AnnotationProcess.class);
    
    private ApplicationContext context;
    private Map<String, DataSourceInfo> dataSourceMap;
    
    /**
     * 构造一个注解处理类
     * @param context spring上下文实例
     */
    public AnnotationProcess(ApplicationContext context, Map<String, DataSourceInfo> dataSourceMap)
    {
        this.context = context;
        this.dataSourceMap = dataSourceMap;
    }
    
    /**
     * 注解处理
     * @param pageMap 所有的Page类
     * @param engine 引擎
     */
    public void scan(Map<String, Page> pageMap,
            SeleniumEngine engine)
    {
        pageProcess(pageMap, engine);
        
        formProcess();
    }

    /**
     * Page类上的注解处理
     * @param pageMap
     * @param engine
     */
    private void pageProcess(Map<String, Page> pageMap, SeleniumEngine engine)
    {
        Map<String, Object> beanMap = context.getBeansWithAnnotation(AutoPage.class);
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
            AbstractElement element = elementProcess(bean, field);

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

            AutoData data = field.getAnnotation(AutoData.class);
            if (data != null) {
                element.putData("data", data.value());
            }
        }
    }

    /**
     * 处理实现来Form接口类的注解
     */
    private void formProcess()
    {
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

    /**
     * Form接口子类中的注解处理
     * @see com.surenpi.autotest.webui.Form
     * @param form 表单实例类
     * @param fields 该表单中所有的字段
     * @param locatorList 表单共用的父级定位信息列表
     */
    private void formFieldAnnoProcess(Form form, Field[] fields, List<Locator> locatorList)
    {
        if(fields == null)
        {
            return;
        }
        
        for(Field field : fields)
        {
            AbstractElement element = elementProcess(form, field);
            if(element == null)
            {
                continue;
            }
            
            element.setStrategy(StrategyType.ZONE.getName());
            element.getLocatorList().addAll(locatorList);
        }
    }

    /**
     * 元素注解处理
     * @see com.surenpi.autotest.webui.ui.AbstractElement
     * @param hostObj 元素实例对象
     * @param field 字段
     * @return 元素实例，如果不是AbstractElement的子类返回null
     */
    private AbstractElement elementProcess(Object hostObj, Field field)
    {
        AbstractElement element = null;
        field.setAccessible(true);
        try
        {
            Object fieldObj = field.get(hostObj);
            if(!(fieldObj instanceof AbstractElement))
            {
                return element;
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
        
        return element;
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
}
