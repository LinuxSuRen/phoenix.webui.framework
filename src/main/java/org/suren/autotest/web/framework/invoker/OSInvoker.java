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

package org.suren.autotest.web.framework.invoker;

import java.io.IOException;

/**
 * 执行本地命令的外部执行类
 * @author suren
 * @since 2016年12月12日 下午12:17:25
 */
public class OSInvoker
{
	/**
	 * 执行本地操作系统命令
	 * @param cmd 系统命令
	 */
	public static void exec(String cmd)
	{
		try
		{
			Runtime.getRuntime().exec(cmd);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
