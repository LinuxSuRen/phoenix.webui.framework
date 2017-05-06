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

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

/**
 * 这里只是测试不会启动真正浏览器的驱动
 * @author suren
 * @date 2017年4月20日 下午10:28:35
 */
public class DriverTest
{

	@Test
	public void htmlUnit()
	{
		WebDriver driver = new HtmlUnitDriver();
		driver.get("http://surenpi.com");
		System.out.println(driver.getTitle());
		driver.quit();
	}
	
	@Test
	public void phantomJS()
	{
		WebDriver driver = new PhantomJSDriver();
		driver.get("http://surenpi.com");
		System.out.println(driver.getTitle());
		driver.quit();
	}

}
