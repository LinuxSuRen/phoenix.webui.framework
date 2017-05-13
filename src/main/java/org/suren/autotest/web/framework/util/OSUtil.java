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

package org.suren.autotest.web.framework.util;

/**
 * 有关操作系统的一些工具方法
 * @author suren
 * @date 2016年12月13日 下午9:55:37
 */
public class OSUtil
{
	/**
	 * 根据系统环境变量PROCESSOR_ARCHITECTURE来判断
	 * @return 包含64返回true，否则返回false
	 */
	public static boolean is64Bit()
	{
		String osArch = System.getenv("PROCESSOR_ARCHITECTURE");
		if(StringUtils.isNotBlank(osArch))
		{
			return osArch.contains("64");
		}
		else
		{
			return false;
		}
	}
}
