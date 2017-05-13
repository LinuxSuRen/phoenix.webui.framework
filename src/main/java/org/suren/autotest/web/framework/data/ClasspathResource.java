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

package org.suren.autotest.web.framework.data;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类路径资源
 * @author suren
 * @date 2016年9月6日 上午8:09:21
 */
public class ClasspathResource implements DataResource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathResource.class);
	
	private Class<?> clz;
	private String resourceName;
	
	public ClasspathResource(Class<?> clz, String resourceName)
	{
		this.clz = clz;
		this.resourceName = resourceName;
	}

	@Override
	public URL getUrl() throws IOException
	{
		URL resUrl = clz.getClassLoader().getResource(resourceName);
		if(resUrl == null) {
			LOGGER.warn(String.format("Can not found resource by [%s].", resourceName));
		}
		
		return resUrl;
	}

}
