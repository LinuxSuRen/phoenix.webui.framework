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

package org.suren.autotest.web.framework.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * @author suren
 * @since 2017年1月24日 上午9:26:26
 */
public class AutotestScope implements Scope
{
	private Map<String, Object> objMap = new HashMap<String, Object>();

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory)
	{
		Object object = objMap.get(name);
		if(object == null)
		{
			object = objectFactory.getObject();
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
