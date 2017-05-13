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
 * 配置文件异常
 * @author suren
 * @date 2016年9月11日 下午8:15:05
 */
public class ConfigException extends AutoTestException
{

	/**  */
	private static final long	serialVersionUID	= 1L;
	
	private final String message;
	
	public ConfigException()
	{
		this.message = "Config exception.";
	}

	/**
	 * 带有自定义消息的构造函数
	 * @param message
	 */
	public ConfigException(String message)
	{
		this.message = message;
	}

	@Override
	public String getMessage()
	{
		return String.format("%s %s", super.getMessage(), this.message);
	}
}
