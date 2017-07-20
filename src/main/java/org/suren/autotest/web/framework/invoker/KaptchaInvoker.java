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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.suren.autotest.interfaces.framework.HttpApiUtil;
import org.suren.autotest.interfaces.framework.param.AtCookie;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * 验证码外部执行器
 * @author suren
 * @since 2017年1月7日 下午9:21:14
 */
public class KaptchaInvoker {
	/**
	 * 获取验证码
	 * @param engine 引擎
	 * @param param 例如：data,http://localhost:8080/G2/captcha!getLastCode.do
	 * @return 验证码
	 */
	public static String execute(SeleniumEngine engine, String param)
	{
		WebDriver driver = engine.getDriver();
		Options manage = driver.manage();
		
		String[] paramArray = param.split(",", 2);
		
		if(paramArray.length != 2)
		{
			throw new RuntimeException("Param format is error, should be 'data,url'");
		}
		
		String key = paramArray[0];
		String url = paramArray[1];
		
		Set<Cookie> cookies = manage.getCookies();
		List<AtCookie> atCookieList = new ArrayList<AtCookie>();
		for(Cookie cookie : cookies)
		{
			String name = cookie.getName();
			String value = cookie.getValue();
			
			AtCookie atCookie = new AtCookie();
			atCookie.setName(name);
			atCookie.setValue(value);
			atCookie.setPath(cookie.getPath());
			atCookie.setDomain(cookie.getDomain());
			
			atCookieList.add(atCookie);
		}
		
		String code = HttpApiUtil.getJsonValue(url, atCookieList, key);
		
		return code;
	}
}
