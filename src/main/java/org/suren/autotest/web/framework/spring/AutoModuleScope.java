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

import com.surenpi.autotest.report.RecordReportWriter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.suren.autotest.web.framework.core.EngineAware;
import org.suren.autotest.web.framework.selenium.WebDriverAware;
import org.suren.autotest.web.framework.settings.AutoModuleProxy;
import org.suren.autotest.web.framework.settings.SettingUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于辅助生成自动化测试报告的scope
 * @author suren
 */
public class AutoModuleScope implements Scope
{
    private final List<RecordReportWriter> recordReportWriters;
    private final Map<String, Object> objMap = new HashMap<String, Object>();
    private SettingUtil util;

    public AutoModuleScope(List<RecordReportWriter> recordReportWriters, SettingUtil util)
    {
        this.recordReportWriters = recordReportWriters;
        this.util = util;
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory)
    {
        Object object = objMap.get(name);
        if(object == null)
        {
            object = objectFactory.getObject();
            AutoModuleProxy proxy = new AutoModuleProxy(object, recordReportWriters);
            object = proxy.getProxy();
            putAware(object);

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
}
