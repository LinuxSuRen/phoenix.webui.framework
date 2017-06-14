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

import java.lang.reflect.Method;

/**
 * @author suren
 */
public class AutoModuleProxy implements MethodInterceptor
{
    private Enhancer enhancer = new Enhancer();
    private Object target;

    public Object getProxy(Object target)
    {
        this.target = target;
        Class<?> clazz = target.getClass();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable
    {
        System.out.println("auto model pre method " + method.getName());
        Object result = methodProxy.invokeSuper(obj, args);
        return result;
    }
}
