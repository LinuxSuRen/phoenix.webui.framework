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

package org.suren.autotest.web.framework.driver;

import java.util.Properties;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.settings.DriverConstants;

/**
 * 需要真正浏览器的驱动单元测试
 * @author suren
 * @date 2017年5月13日 下午10:04:17
 */
public class RealDriverTest
{
	private String testUrl = "http://surenpi.com";
	
	@Test
	public void phantomJS()
	{
		WebDriver driver = new PhantomJSDriver();
		driver.get(testUrl);
		driver.quit();
	}
	
	@Test
	public void internetExplorer()
	{
		SeleniumEngine engine = new SeleniumEngine(){

			@Override
			public void beforeStart(Properties enginePro)
			{
				super.beforeStart(enginePro);
				
				enginePro.put(DriverConstants.INITIAL_URL, "http://baidu.com");
			}
		};
		engine.setDriverStr(DriverConstants.DRIVER_IE);
		engine.init();
		
		engine.openUrl("http://surenpi.com");
		
		engine.delayClose(2000);
	}

}
