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

package org.suren.autotest.web.framework.selenium;

import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_CHROME;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_FIREFOX;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_IE;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_OPERA;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_PHANTOM_JS;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_SAFARI;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suren.autotest.web.framework.settings.DriverConstants;
import org.suren.autotest.web.framework.util.BrowserUtil;

/**
 * 浏览器配置
 * @author <a href="http://surenpi.com">suren</a>
 */
public class CapabilityConfig
{
	private static final Logger logger = LoggerFactory.getLogger(CapabilityConfig.class);
	
	private Map<String, DesiredCapabilities> engineCapMap;
	private Properties enginePro; //引擎参数集合

	/**
	 * @param engineCapMap 用于保存浏览器配置的返回结果
	 * @param enginePro 引擎配置集合
	 */
	public CapabilityConfig(Map<String, DesiredCapabilities> engineCapMap,
			Properties enginePro)
	{
		this.engineCapMap = engineCapMap;
		this.enginePro = enginePro;
	}

	/**
	 * 加载所有支持浏览器的配置
	 */
	public void config()
	{
		firefox();
		
		chrome();
		
		ie();
		
		{
			String proFile = System.getProperty("firefox.profile", null);
			FirefoxProfile profile = new FirefoxProfile(proFile != null ? new File(proFile) : null);
			fireFoxPreSet(profile);
		}
		
		{
			DesiredCapabilities capability = DesiredCapabilities.safari();
			engineCapMap.put(DRIVER_SAFARI, capability);
		}
		
		{
			DesiredCapabilities capability = DesiredCapabilities.operaBlink();
			engineCapMap.put(DRIVER_OPERA, capability);
		}
		
		{
			DesiredCapabilities capability = DesiredCapabilities.phantomjs();
			engineCapMap.put(DRIVER_PHANTOM_JS, capability);
		}
	}
	
	/**
	 * 火狐浏览器配置
	 */
	private void firefox()
	{
		DesiredCapabilities capability = DesiredCapabilities.firefox();
		capability.setCapability("marionette", true);
		engineCapMap.put(DRIVER_FIREFOX, capability);
	}
	
	/**
	 * 谷歌浏览器
	 * chrome://version/
	 */
	private void chrome()
	{
		DesiredCapabilities capability = DesiredCapabilities.chrome();
		
		ChromeOptions options = new ChromeOptions();
		Iterator<Object> chromeKeys = enginePro.keySet().iterator();
		Proxy proxy = new Proxy();
		while(chromeKeys.hasNext())
		{
			String key = chromeKeys.next().toString();
			if(!key.startsWith("chrome"))
			{
				continue;
			}
			
			if(key.startsWith("chrome.args"))
			{
				String arg = key.replace("chrome.args.", "") + "=" + enginePro.getProperty(key);
				if(arg.endsWith("="))
				{
					arg = arg.substring(0, arg.length() - 1);
				}
				options.addArguments(arg);
				logger.info(String.format("chrome arguments : [%s]", arg));
			}
			else if(key.startsWith("chrome.cap.proxy.http"))
			{
				String val = enginePro.getProperty(key);
				
				proxy.setHttpProxy(val);
			}
			else if(key.startsWith("chrome.cap.proxy.ftp"))
			{
				String val = enginePro.getProperty(key);
				
				proxy.setFtpProxy(val);
			}
			else if(key.startsWith("chrome.cap.proxy.socks"))
			{
				String val = enginePro.getProperty(key);
				
				proxy.setSocksProxy(val);
			}
			else if(key.startsWith("chrome.cap.proxy.socks.username"))
			{
				String val = enginePro.getProperty(key);
				
				proxy.setSocksUsername(val);
			}
			else if(key.startsWith("chrome.cap.proxy.socks.password"))
			{
				String val = enginePro.getProperty(key);
				
				proxy.setSocksPassword(val);
			}
			else if(key.startsWith("chrome.binary"))
			{
				options.setBinary(enginePro.getProperty(key));
			}
		}
		
		if("true".equals(enginePro.getProperty("chrome.cap.proxy.enable")))
		{
			capability.setCapability("proxy", proxy);
		}
		capability.setCapability(ChromeOptions.CAPABILITY, options);
	
		engineCapMap.put(DRIVER_CHROME, capability);
	}
	
	/**
	 * ie浏览器
	 */
	private void ie()
	{
		String initialUrl = enginePro.getProperty(DriverConstants.INITIAL_URL,
				"http://surenpi.com");
		
		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
		capability.setCapability(
				InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capability.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, initialUrl);
		capability.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
		engineCapMap.put(DRIVER_IE, capability);
	}

	/**
	 * 设定firefox首选项
	 * @param profile
	 */
	private void fireFoxPreSet(FirefoxProfile profile)
	{
		BrowserUtil browserUtil = new BrowserUtil();
		Map<String, Boolean> boolMap = browserUtil.getFirefoxPreBoolMap();
		Iterator<String> boolIt = boolMap.keySet().iterator();
		while(boolIt.hasNext())
		{
			String key = boolIt.next();
			
			profile.setPreference(key, boolMap.get(key));
		}
		
		Map<String, Integer> intMap = browserUtil.getFirefoxPreIntMap();
		Iterator<String> intIt = intMap.keySet().iterator();
		while(intIt.hasNext())
		{
			String key = intIt.next();
			
			profile.setPreference(key, intMap.get(key));
		}
		
		Map<String, Integer> strMap = browserUtil.getFirefoxPreIntMap();
		Iterator<String> strIt = intMap.keySet().iterator();
		while(strIt.hasNext())
		{
			String key = strIt.next();
			
			profile.setPreference(key, strMap.get(key));
		}
	}
}
