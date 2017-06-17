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

package org.suren.autotest.web.framework.settings;

import net.sf.cglib.core.GeneratorStrategy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.suren.autotest.web.framework.annotation.AutoModule;
import org.suren.autotest.web.framework.report.RecordReportWriter;
import org.suren.autotest.web.framework.report.record.ExceptionRecord;
import org.suren.autotest.web.framework.report.record.NormalRecord;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 模块代理类
 * @author suren
 */
public class AutoModuleProxy implements MethodInterceptor
{
    private Enhancer enhancer = new Enhancer();
    private Object target;
    private List<RecordReportWriter> recordReportWriters;

    public AutoModuleProxy(Object target, List<RecordReportWriter> recordReportWriters)
    {
        this.target = target;
        this.recordReportWriters = recordReportWriters;
    }

    public Object getProxy()
    {
        Class<?> clazz = target.getClass();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable
    {
        long beginTime = System.currentTimeMillis();

        Object result = null;
        Class<?> superCls = obj.getClass().getSuperclass();
        AutoModule autoModule = superCls.getAnnotation(AutoModule.class);

        NormalRecord normalRecord = new NormalRecord();
        normalRecord.setBeginTime(beginTime);
        normalRecord.setClazzName(superCls.getName());
        normalRecord.setMethodName(method.getName());
        normalRecord.setModuleName(autoModule.name());
        normalRecord.setModuleDescription(autoModule.description());

        try
        {
            result = methodProxy.invokeSuper(obj, args);

            normalRecord.setEndTime(System.currentTimeMillis());

            if(isNotExcludeMethod(method))
            {
                write(normalRecord);
            }
        }
        catch(Exception e)
        {
            normalRecord.setEndTime(System.currentTimeMillis());
            write(new ExceptionRecord(e, normalRecord));

            throw e;
        }

        return result;
    }

    private boolean isNotExcludeMethod(Method method)
    {
        String name = method.getName();
        if("setEngine".equals(name))
        {
            return false;
        }

        if("setWebDriver".equals(name))
        {
            return false;
        }

        return true;
    }

    private void write(NormalRecord record)
    {
        for(RecordReportWriter writer : recordReportWriters)
        {
            writer.write(record);
        }
    }

    private void write(ExceptionRecord record)
    {
        for(RecordReportWriter writer : recordReportWriters)
        {
            writer.write(record);
        }
    }
}
