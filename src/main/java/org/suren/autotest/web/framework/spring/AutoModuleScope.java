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

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.suren.autotest.web.framework.settings.AutoModuleProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author suren
 */
public class AutoModuleScope implements Scope
{
    private Map<String, Object> objMap = new HashMap<String, Object>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory)
    {
        Object object = objMap.get(name);
        if(object == null)
        {
            object = objectFactory.getObject();
            AutoModuleProxy proxy = new AutoModuleProxy();
            Object proxyObj = proxy.getProxy(object);
            for(Field field : object.getClass().getDeclaredFields())
            {
                field.setAccessible(true);
                try {
                    Field proxyField = proxyObj.getClass().getDeclaredField(field.getName());
                    proxyField.setAccessible(true);

                    proxyField.set(proxyObj, field.get(object));
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            objMap.put(name, object);
        }

        return object;
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
