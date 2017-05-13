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

package org.suren.autotest.web.framework.core;

/**
 * 找不到配置文件异常
 * @author suren
 * @date 2016年11月24日 上午8:22:50
 */
public class ConfigNotFoundException extends ConfigException
{

	/**  */
	private static final long	serialVersionUID	= 1L;

	private String type;
	private String configPath;
	private String message;
	
	public ConfigNotFoundException(String message)
	{
		this.message = message;
	}
	
	/**
	 * @param type
	 * @param configPath
	 */
	public ConfigNotFoundException(String type, String configPath)
	{
		this.type = type;
		this.configPath = configPath;
	}

	@Override
	public String getMessage()
	{
		message = (message != null) ? message : String.format("Can not found config file for AutoTest,"
				+ " type [%s], path [%s].", type, configPath);
		
		return message;
	}

}
