/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.suren.autotest.web.framework.selenium.WebDriverAware;
import org.suren.autotest.web.framework.settings.AutoModuleProxy;
import org.suren.autotest.web.framework.settings.Phoenix;

import com.surenpi.autotest.report.RecordReportWriter;
import com.surenpi.autotest.webui.core.EngineAware;

/**
 * 用于辅助生成自动化测试报告的scope
 * @author suren
 */
public class AutoModuleScope implements Scope, ApplicationContextAware
{
//    @Autowired
    private List<RecordReportWriter> recordReportWriters;
    private final Map<String, Object> objMap = new HashMap<String, Object>();
    private Phoenix util;

    // comment here due to the inject order issues
//    public AutoModuleScope(List<RecordReportWriter> recordReportWriters, Phoenix util)
//    {
//        this.recordReportWriters = recordReportWriters;
//        this.util = util;
//    }

    public AutoModuleScope() {
//        RecordReportWriter.class
    }

    private ApplicationContext context;
    public AutoModuleScope(ApplicationContext context, Phoenix phoenix) {
        this.util = phoenix;
        this.context = context;
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory)
    {
//        Map<String, RecordReportWriter> reportWriters = util.getRunner(RecordReportWriter.class);
//        recordReportWriters = reportWriters.values().parallelStream().collect(Collectors.toList());

        Object object = objMap.get(name);
        if(object == null)
        {
            object = objectFactory.getObject();
            Map<String, RecordReportWriter> map = context.getBeansOfType(RecordReportWriter.class);
            recordReportWriters = new ArrayList<>();
            map.forEach((k,v) -> {
                recordReportWriters.add(v);
            });
            AutoModuleProxy proxy = new AutoModuleProxy(object, recordReportWriters, util, context);
            Object proxyObject = proxy.getProxy();

            BeanCopier beanCopier = BeanCopier.create(object.getClass(), proxyObject.getClass(), false);
            beanCopier.copy(object, proxyObject, null);

            putAware(proxyObject);

            object = proxyObject;
            objMap.put(name, object);
        }

        return object;
    }

    /**
     * 向目标对象注入特定接口实例
     * @param target
     */
    private void putAware(Object target)
    {
        if(EngineAware.class.isAssignableFrom(target.getClass().getSuperclass()))
        {
            ((EngineAware) target).setEngine(util);
        }

        if(WebDriverAware.class.isAssignableFrom(target.getClass().getSuperclass()))
        {
            ((WebDriverAware) target).setWebDriver(util.getEngine().getDriver());
        }
    }

    @Override
    public Object remove(String name)
    {
        return objMap.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback)
    {
    }

    @Override
    public Object resolveContextualObject(String key)
    {
        return null;
    }

    @Override
    public String getConversationId()
    {
        return null;
    }

    public List<RecordReportWriter> getRecordReportWriters() {
        return recordReportWriters;
    }

    public void setRecordReportWriters(List<RecordReportWriter> recordReportWriters) {
        this.recordReportWriters = recordReportWriters;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
