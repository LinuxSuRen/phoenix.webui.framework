/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.selenium;

import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_CHROME;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_FIREFOX;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_HTML_UNIT;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_IE;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_OPERA;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_PHANTOM_JS;
import static org.suren.autotest.web.framework.settings.DriverConstants.DRIVER_SAFARI;
import static org.suren.autotest.web.framework.settings.DriverConstants.ENGINE_CONFIG_FILE_NAME;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.settings.DriverConstants;
import org.suren.autotest.web.framework.util.StringUtils;
import org.suren.autotest.web.framework.util.ThreadUtil;
import org.suren.autotest.webdriver.downloader.DriverDownloader;
import org.suren.autotest.webdriver.downloader.DriverMapping;
import org.suren.autotest.webdriver.downloader.PathUtil;

import com.surenpi.autotest.datasource.DynamicData;
import com.surenpi.autotest.webui.core.AutoTestException;

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
	
	@Autowired
	private List<DynamicData> dynamicDataList;
	
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
		    logger.error("初始化配置文件发生错误！", e);
		}
	}
	
	/**
	 * 浏览器启动前的回调函数
	 * @param enginePro 回调函数
	 */
	public void beforeStart(Properties enginePro){};
	
	/**
	 * 浏览器引擎初始化
	 */
	public void init()
	{
		initConfig();
		
		beforeStart(enginePro);
		
		new CapabilityConfig(engineCapMap, enginePro).config();
		
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
				throw new AutoTestException(e.getMessage());
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
			driver = new HtmlUnitDriver(true);
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
	
	/**
	 * @param key key
	 * @param identify identify
	 */
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

		String autoLoad = enginePro.getProperty("engine.autoLoad", "true");
		if("true".equals(autoLoad))
		{
			loadDriverFromMapping(classLoader, enginePro);
		}
		else
		{
			logger.warn("You have turn engine.autoLoad off, you need set webdriver manual.");
		}
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
			
			String driverPath = null;
			try
			{
				driverPath = new DriverDownloader().getLocalFilePath(driverURL);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
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
	 * @return 结果
	 */
	public boolean storePro()
	{
		ClassLoader classLoader = this.getClass().getClassLoader();
		
		return storePro(classLoader);
	}
	
	/**
	 * @param classLoader classLoader
	 * @return classLoader
	 */
	public boolean storePro(ClassLoader classLoader)
	{
	    String encoding = "utf-8";
		URL defaultResourceUrl = classLoader.getResource(ENGINE_CONFIG_FILE_NAME);
		try(OutputStream out = new FileOutputStream(
				new File(URLDecoder.decode(defaultResourceUrl.getFile(), encoding))))
		{
			enginePro.store(out, "");
			
			return true;
		}
		catch (UnsupportedEncodingException e)
		{
		    logger.error("不支持的字符集" + encoding, e);
		}
		catch (FileNotFoundException e)
		{
            logger.error("", e);
		}
		catch (IOException e)
		{
            logger.error("", e);
		}
		
		return false;
	}
	
	/**
	 * 转为为初始化的驱动
	 * @param driver driver
	 * @return driver
	 */
	public WebDriver turnToRootDriver(WebDriver driver)
	{
		return driver.switchTo().defaultContent();
	}

	/**
	 * 打开指定地址
	 * @param url url
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
	 * 延迟关闭
	 * @param timeout 单位：毫秒
	 */
	public void delayClose(long timeout)
	{
		ThreadUtil.silentSleep(timeout);
		
		close();
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
				    logger.error("", e);
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
	 * @param driverStr driverStr
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
        if(remoteStr == null)
        {
            return;
        }
        
        for(DynamicData dynamicData : dynamicDataList)
        {
            if("system".equals(dynamicData.getType()))
            {
                this.remoteStr = dynamicData.getValue(remoteStr);
                break;
            }
        }
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
	 * @param timeout timeout
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
	 * @param fullScreen fullScreen
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
	 * @param width width
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
	 * @param height height
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
	 * @return 高度
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
