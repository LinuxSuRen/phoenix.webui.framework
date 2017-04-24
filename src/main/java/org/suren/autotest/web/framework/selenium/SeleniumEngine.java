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
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_SAFARI;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_PHANTOM_JS;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_HTML_UNIT;
import static org.suren.autotest.web.framework.settings.DriverConstants.ENGINE_CONFIG_FILE_NAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.AutoTestException;
import org.suren.autotest.web.framework.settings.DriverConstants;
import org.suren.autotest.web.framework.util.BrowserUtil;
import org.suren.autotest.web.framework.util.PathUtil;
import org.suren.autotest.web.framework.util.StringUtils;

/**
 * 浏览器引擎封装类
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
@Scope(value = "autotest", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SeleniumEngine
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumEngine.class);

	private final Properties enginePro = new Properties(); //引擎参数集合
	private final Map<String, Object> dataMap = new HashMap<String, Object>();
	
	private Map<String, DesiredCapabilities> engineCapMap = new HashMap<String, DesiredCapabilities>();
	
	private WebDriver	driver;
	private String		driverStr;
	private String 		remoteStr;
	private long		timeout;
	private boolean		fullScreen;
	private boolean 	maximize;
	private int			width;
	private int			height;
	private int			toolbarHeight;
	
	public SeleniumEngine(){}
	
	/**
	 * 初始化配置文件
	 */
	public void initConfig()
	{
		try
		{
			ClassLoader classLoader = this.getClass().getClassLoader();
			
			loadDefaultEnginePath(classLoader, enginePro); //加载默认配置
			
			System.getProperties().putAll(enginePro);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 浏览器引擎初始化
	 */
	public void init()
	{
		initConfig();
		
		initCapMap();
		
		String curDriverStr = getDriverStr();
		DesiredCapabilities capability = engineCapMap.get(curDriverStr);
		
		if(StringUtils.isNotBlank(getRemoteStr()))
		{
			try
			{
				driver = new RemoteWebDriver(new URL(getRemoteStr()), capability);
			}
			catch (MalformedURLException e)
			{
				throw new AutoTestException();
			}
		}
		else if(DRIVER_CHROME.equals(curDriverStr))
		{
			driver = new ChromeDriver(capability);
		}
		else if(DRIVER_IE.equals(curDriverStr))
		{
			driver = new InternetExplorerDriver(capability);
		}
		else if(DRIVER_FIREFOX.equals(curDriverStr))
		{
//			String proFile = System.getProperty("firefox.profile", null);
//			FirefoxProfile profile = new FirefoxProfile(proFile != null ? new File(proFile) : null);
//			fireFoxPreSet(profile);
//			FirefoxOptions firefoxOptions = new FirefoxOptions();
//			firefoxOptions.setProfile(new FirefoxProfile());
//			driver = new FirefoxDriver(firefoxOptions);
//			FirefoxProfile profile = new FirefoxProfile();
//			profile.setPreference("browser.tabs.remote.autostart", false);
//			profile.setPreference("browser.tabs.remote.autostart.1", false);
//			profile.setPreference("browser.tabs.remote.autostart.2", false);
//			profile.setPreference("browser.tabs.remote.force-enable", false);
//			capability.setCapability(FirefoxDriver.PROFILE, profile);
			driver = new FirefoxDriver(capability);
		}
		else if(DRIVER_SAFARI.equals(curDriverStr))
		{
			driver = new SafariDriver(capability);
		}
		else if(DRIVER_OPERA.equals(curDriverStr))
		{
			driver = new OperaDriver(capability);
		}
		else if(DRIVER_PHANTOM_JS.equals(curDriverStr))
		{
			driver = new PhantomJSDriver(capability);
		}
		else if(DRIVER_HTML_UNIT.equals(curDriverStr))
		{
			driver = new HtmlUnitDriver();
		}
		else
		{
			throw new RuntimeException(String.format("Unknow type driver [%s].", curDriverStr));
		}
		
		if(timeout > 0)
		{
			driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		}
		
		Window window = driver.manage().window();
		if(fullScreen)
		{
			try
			{
//				window.fullscreen();
			}
			catch(UnsupportedCommandException e)
			{
				logger.error("Unsupported fullScreen command.", e);
			}
		}
		
		// 处理cookie
		Options manage = driver.manage();
		boolean cookieLoad = Boolean.parseBoolean(enginePro.getProperty("cookie.load", "false"));
		File root = PathUtil.getRootDir();
		File cookieFile = new File(root, enginePro.getProperty("cookie.save.path", "phoenix.autotest.cookie"));
		
		if(cookieLoad)
		{
			try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(cookieFile)))
			{
				Object cookiesObj = input.readObject();
				if(cookiesObj != null && cookiesObj instanceof Set<?>)
				{
					Set<Cookie> cookies =  (Set<Cookie>) cookiesObj;
					for(Cookie cookie : cookies)
					{
						manage.addCookie(cookie);
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		
		if(maximize)
		{
			window.maximize();
		}
		
		if(getWidth() > 0)
		{
			window.setSize(new Dimension(getWidth(), window.getSize().getHeight()));
		}
		
		if(getHeight() > 0)
		{
			window.setSize(new Dimension(window.getSize().getWidth(), getHeight()));
		}
	}
	
	/**
	 * @return 引擎中的配置参数
	 */
	public Map<Object, Object> getEngineConfig()
	{
		return Collections.unmodifiableMap(enginePro == null ? new Properties() : enginePro);
	}
	
	public void setProgressId(String key, String identify)
	{
		enginePro.put(key, identify);
	}
	
	/**
	 * @return 谷歌浏览器版本
	 */
	public String getChromeVer()
	{
		return enginePro.getProperty(DriverConstants.DRIVER_CHROME + ".version");
	}
	
	public void setChromeVer(String ver)
	{
		enginePro.setProperty(DriverConstants.DRIVER_CHROME + ".version", ver);
	}
	
	/**
	 * 初始化所有浏览器的配置
	 */
	private void initCapMap()
	{
		{
			DesiredCapabilities capability = DesiredCapabilities.firefox();
			capability.setCapability("marionette", true);
			engineCapMap.put(DRIVER_FIREFOX, capability);
		}
		
		//chrome://version/
		{
			DesiredCapabilities capability = DesiredCapabilities.chrome();
			
			ChromeOptions options = new ChromeOptions();
			Iterator<Object> chromeKeys = enginePro.keySet().iterator();
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
					
					Proxy proxy = new Proxy();
					proxy.setHttpProxy(val);
					capability.setCapability("proxy", proxy);
				}
				else if(key.startsWith("chrome.binary"))
				{
					options.setBinary(enginePro.getProperty(key));
				}
			}
			capability.setCapability(ChromeOptions.CAPABILITY, options);
		
			engineCapMap.put(DRIVER_CHROME, capability);
		}
		
		{
			DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
			capability.setCapability(
					InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capability.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "http://surenpi.com");
			capability.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
			engineCapMap.put(DRIVER_IE, capability);
		}
		
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

	/**
	 * 加载默认的engine
	 * @param enginePro
	 * @throws MalformedURLException 
	 */
	private void loadDefaultEnginePath(ClassLoader classLoader, Properties enginePro) throws MalformedURLException
	{
		Enumeration<URL> resurceUrls = null;
		URL defaultResourceUrl = null;
		try
		{
			resurceUrls = classLoader.getResources(ENGINE_CONFIG_FILE_NAME);
			defaultResourceUrl = classLoader.getResource(ENGINE_CONFIG_FILE_NAME);
		}
		catch (IOException e)
		{
			logger.error("Engine properties loading error.", e);
		}
		
		if(resurceUrls == null)
		{
			return;
		}
		
		//这里包含所有的，包括jar中的
		while(resurceUrls.hasMoreElements())
		{
			URL url = resurceUrls.nextElement();

			try
			{
				loadFromURL(enginePro, url);
			}
			catch (IOException e)
			{
				logger.error("loading engine error.", e);
			}
		}

		try
		{
			//这里是运行类路径下的
			loadFromURL(enginePro, defaultResourceUrl);
		}
		catch (IOException e)
		{
			logger.error("loading engine error.", e);
		}
		
		loadDriverFromMapping(classLoader, enginePro);
	}
	
	/**
	 * 从映射文件中，动态获取当前需要加载的浏览器驱动
	 * @param classLoader
	 * @param enginePro
	 * @throws MalformedURLException 
	 */
	private void loadDriverFromMapping(ClassLoader classLoader, Properties enginePro)
			throws MalformedURLException
	{
		//实现对多个操作系统的兼容性设置
		final String os = System.getProperty("os.name");
		final String arch = System.getProperty("os.arch");
		final String curDriverStr = getDriverStr();
		
		String commonOs = enginePro.getProperty("os.map.name." + os);
		String commonArch = enginePro.getProperty("os.map.arch." + arch);
		
		if(StringUtils.isAnyBlank(commonOs, commonArch))
		{
			throw new RuntimeException(String.format("unknow os [%s] and arch [%s].", os, arch));
		}
		
		final String ver = enginePro.getProperty(curDriverStr + ".version");
		
		DriverMapping driverMapping = new DriverMapping();
		driverMapping.init();
		
		URL driverURL = null;
		String remoteDriverUrl = driverMapping.getUrl(curDriverStr, ver, commonOs, commonArch);
		if(remoteDriverUrl == null)
		{
			logger.error(String.format("Can not found remote driver url, browser is"
					+ " [%s], version is [%s], os is [%s], arch is [%s].",
					curDriverStr, ver, commonOs, commonArch));
		}
		else
		{
			driverURL = new URL(remoteDriverUrl);
			
			String driverPath = getLocalFilePath(driverURL);
			if(StringUtils.isBlank(driverPath))
			{
				throw new RuntimeException("Driver path is empty!");
			}
			
			enginePro.put(String.format("webdriver.%s.driver", curDriverStr), driverPath);
		}
	}

	private void loadFromURL(Properties enginePro, URL url) throws IOException
	{
		try(InputStream inputStream = url.openStream())
		{
			enginePro.load(inputStream);
		}
	}
	
	/**
	 * 从指定地址中获取驱动文件，并拷贝到框架根目录中。如果是zip格式的话，会自动解压。
	 * @param url
	 * @return 获取到的驱动文件绝对路径，如果没有找到返回空字符串
	 */
	private String getLocalFilePath(URL url)
	{
		if(url == null)
		{
			return "";
		}
		
 		final String driverPrefix = "surenpi.com."; 
		
		File driverFile = null;
		String protocol = url.getProtocol();
		if("jar".equals(protocol) || "http".equals(protocol))
		{
			String driverFileName = (driverPrefix + new File(url.getFile()).getName());
			try(InputStream inputStream = url.openStream())
			{
				driverFile = PathUtil.copyFileToRoot(inputStream, driverFileName);
			}
			catch (IOException e)
			{
				logger.error("Driver file copy error.", e);
			}
		}
		else
		{
			try
			{
				driverFile = new File(URLDecoder.decode(url.getFile(), "utf-8"));
			}
			catch (UnsupportedEncodingException e)
			{
				logger.error(e.getMessage(), e);
			}
		}
		
		return fileProcess(driverFile, driverPrefix);
	}
	
	/**
	 * 处理经过压缩和没有压缩的文件
	 * @param driverFile
	 * @param driverPrefix
	 * @return 返回非压缩文件的绝对路径
	 */
	private String fileProcess(File driverFile, String driverPrefix)
	{
		if(driverFile != null && driverFile.isFile())
		{
			//如果是以.zip为后缀的文件，则自动解压
			if(driverFile.getName().endsWith(".zip"))
			{
				try(ZipInputStream zipIn = new ZipInputStream(new FileInputStream(driverFile)))
				{
					ZipEntry entry = zipIn.getNextEntry();
					if(entry != null)
					{
						driverFile = new File(driverFile.getParent(), driverPrefix + entry.getName());
						
						if(needCopy(driverFile, entry.getSize()))
						{
							PathUtil.copyFileToRoot(zipIn, driverFile);
						}
					}
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			return driverFile.getAbsolutePath();
		}
		else
		{
			return "";
		}
	}

	/**
	 * 是否需要拷贝
	 * @param driverFile
	 * @param orginalSize
	 * @return
	 */
	private boolean needCopy(File driverFile, long orginalSize)
	{
		if(driverFile.isDirectory())
		{
			String[] childList = driverFile.list();
			
			if((childList != null && childList.length > 0) || !driverFile.delete())
			{
				throw new RuntimeException(String.format("Directory [%s] is not empty or can not bean delete.", driverFile.getAbsolutePath()));
			}
		}
		
		if(!driverFile.isFile() || driverFile.length() != orginalSize)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * @return
	 */
	public boolean storePro()
	{
		ClassLoader classLoader = this.getClass().getClassLoader();
		
		return storePro(classLoader);
	}
	
	/**
	 * @param classLoader
	 * @return
	 */
	public boolean storePro(ClassLoader classLoader)
	{
		URL defaultResourceUrl = classLoader.getResource(ENGINE_CONFIG_FILE_NAME);
		try(OutputStream out = new FileOutputStream(
				new File(URLDecoder.decode(defaultResourceUrl.getFile(), "utf-8"))))
		{
			enginePro.store(out, "");
			
			return true;
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 转为为初始化的驱动
	 * @param driver
	 * @return
	 */
	public WebDriver turnToRootDriver(WebDriver driver)
	{
		return driver.switchTo().defaultContent();
	}

	/**
	 * 打开指定地址
	 * @param url
	 */
	public void openUrl(String url)
	{
		driver.get(url);
		
		dataMap.put("sys.startUrl", url);
	}
	
	/**
	 * @return 系统数据配置，不可修改
	 */
	public Map<String, Object> getDataMap()
	{
		return Collections.unmodifiableMap(dataMap);
	}

	/**
	 * 关闭浏览器引擎
	 */
	public void close()
	{
		if(driver != null)
		{
			Options manage = driver.manage();
			boolean cookieSave = Boolean.parseBoolean(enginePro.getProperty("cookie.save", "false"));
			File root = PathUtil.getRootDir();
			File cookieFile = new File(root, enginePro.getProperty("cookie.save.path", "phoenix.autotest.cookie"));
			
			if(cookieSave)
			{
				Set<Cookie> cookies = manage.getCookies();
				try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(cookieFile)))
				{
					output.writeObject(cookies);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			driver.quit();
		}
	}

	/**
	 * @return 引擎对象
	 */
	public WebDriver getDriver()
	{
		return driver;
	}

	/**
	 * @return 引擎名称
	 */
	public String getDriverStr()
	{
		return driverStr;
	}

	/**
	 * 设置引擎名称
	 * @param driverStr
	 */
	public void setDriverStr(String driverStr)
	{
		this.driverStr = driverStr;
	}

	public String getRemoteStr()
	{
		return remoteStr;
	}

	public void setRemoteStr(String remoteStr)
	{
		this.remoteStr = remoteStr;
	}

	/**
	 * @return 超时时间
	 */
	public long getTimeout()
	{
		return timeout;
	}

	/**
	 * 设定超时时间
	 * @param timeout
	 */
	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	/**
	 * @return 全屏返回true，否则返回false
	 */
	public boolean isFullScreen()
	{
		return fullScreen;
	}

	/**
	 * 设置是否要全屏
	 * @param fullScreen
	 */
	public void setFullScreen(boolean fullScreen)
	{
		this.fullScreen = fullScreen;
	}

	/**
	 * @return 浏览器宽度
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * 设置浏览器宽度
	 * @param width
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return 浏览器高度
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * 设置浏览器高度
	 * @param height
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return the maximize
	 */
	public boolean isMaximize()
	{
		return maximize;
	}

	/**
	 * @param maximize the maximize to set
	 */
	public void setMaximize(boolean maximize)
	{
		this.maximize = maximize;
	}
	
	/**
	 * 计算工具栏高度
	 * @return
	 */
	public int computeToolbarHeight()
	{
		JavascriptExecutor jsExe = (JavascriptExecutor) driver;
		Object objectHeight = jsExe.executeScript("return window.outerHeight - window.innerHeight;");
		if(objectHeight instanceof Long)
		{
			toolbarHeight = ((Long) objectHeight).intValue();
		}

		return toolbarHeight;
	}

	/**
	 * @return the toolbarHeight
	 */
	public int getToolbarHeight()
	{
		return toolbarHeight;
	}
}
