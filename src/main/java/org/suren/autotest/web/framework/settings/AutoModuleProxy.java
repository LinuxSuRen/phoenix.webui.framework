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

import com.surenpi.autotest.report.RecordReportWriter;
import com.surenpi.autotest.report.record.ExceptionRecord;
import com.surenpi.autotest.report.record.NormalRecord;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.suren.autotest.web.framework.annotation.AutoExpect;
import org.suren.autotest.web.framework.annotation.AutoModule;

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
        AutoExpect autoExpect = method.getAnnotation(AutoExpect.class);

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
        catch(Exception | AssertionError e)
        {
            boolean acceptException = exceptionHandle(autoExpect, e);

            normalRecord.setEndTime(System.currentTimeMillis());
            write(new ExceptionRecord(e, normalRecord));

            if(acceptException)
            {
                e.printStackTrace();
            }
            else
            {
                throw e;
            }
        }

        return result;
    }

    /**
     * 根据注解配置，是否要对异常进行处理
     * @param autoExpect
     * @return
     */
    private boolean exceptionHandle(AutoExpect autoExpect, Throwable e)
    {
        if(autoExpect != null)
        {
            Class<?>[] acceptArray = autoExpect.accept();
            if(acceptArray != null && acceptArray.length > 0)
            {
                for(Class<?> clz : acceptArray)
                {
                    if(clz.equals(e))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
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
