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

package org.suren.autotest.web.framework.hook;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suren.autotest.web.framework.settings.Phoenix;

/**
 * 为了防止在程序意外关闭或者是用户没有显示地调用关闭浏览器的api导致的资源没有释放
 * @author <a href="http://surenpi.com">suren</a>
 */
public class ShutdownHook extends Thread
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownHook.class);
	
	private Phoenix settingUtil;
	
	/**
	 * @param context 上下文
	 */
	public ShutdownHook(Phoenix context)
	{
		this.settingUtil = context;
		LOGGER.info("egnine close hook already regist.");
	}

	@Override
	public void run()
	{
		LOGGER.info("prepare to execute engine close operation.");
		
		try
		{
			settingUtil.close();
		}
		catch (IOException e)
		{
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("engine closed successful.");
	}

}
