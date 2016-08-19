/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import static org.suren.autotest.web.framework.settings.DriverConstants.*;

/**
 * 浏览器引擎封装类
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
public class SeleniumEngine
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumEngine.class);
	
	private String		driverStr;
	private WebDriver	driver;
	private long		timeout;
	private boolean		fullScreen;
	private int			width;
	private int			height;
	
	/**
	 * 浏览器引擎初始化
	 */
	public void init()
	{
		InputStream  inputStream = null;
		try
		{
			Properties enginePro = new Properties();
			ClassLoader classLoader = this.getClass().getClassLoader();
			
			loadDefaultEnginePath(classLoader, enginePro); //加载默认配置
			
			System.getProperties().putAll(enginePro);
		}
		finally
		{
			IOUtils.closeQuietly(inputStream);
		}
		
		String curDriverStr = getDriverStr();
		if(DRIVER_CHROME.equals(curDriverStr))
		{
			driver = new ChromeDriver();
		}
		else if(DRIVER_IE.equals(curDriverStr))
		{
			DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
			capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			driver = new InternetExplorerDriver(capability);
		}
		else if(DRIVER_FIREFOX.equals(curDriverStr))
		{
			FirefoxProfile profile = new FirefoxProfile(new File("C:\\Users\\zhaoxj\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles\\8qtjpqgj.default"));
			driver = new FirefoxDriver(profile);
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
				window.fullscreen();
			}
			catch(UnsupportedCommandException e)
			{
				logger.error("Unsupported fullScreen command.", e);
			}
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
	 * 加载默认的engine
	 * @param enginePro
	 */
	private void loadDefaultEnginePath(ClassLoader classLoader, Properties enginePro) {
		URL ieDriverURL = classLoader.getResource("IEDriverServer.exe");
		URL chromeDrvierURL = classLoader.getResource("chromedriver.exe");
		
		enginePro.put("webdriver.ie.driver", getLocalFilePath(ieDriverURL));
		enginePro.put("webdriver.chrome.driver", getLocalFilePath(chromeDrvierURL));
		
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

		try
		{
			loadFromURL(enginePro, defaultResourceUrl);
		}
		catch (IOException e)
		{
			logger.error("loading engine error.", e);
		}
		
		while(resurceUrls.hasMoreElements())
		{
			URL url = resurceUrls.nextElement();
			if(url == defaultResourceUrl)
			{
				continue;
			}

			try
			{
				loadFromURL(enginePro, url);
			}
			catch (IOException e)
			{
				logger.error("loading engine error.", e);
			}
		}
		
		String os = System.getProperty("os.name");
		if(!"Linux".equals(os))
		{
		}
		else
		{
		}
	}
	
	private void loadFromURL(Properties enginePro, URL url) throws IOException
	{
		try(InputStream inputStream = url.openStream())
		{
			enginePro.load(inputStream);
		}
	}
	
	private String getLocalFilePath(URL url)
	{
		if(url == null)
		{
			return "";
		}
		
		File driverFile = null;
		if("jar".equals(url.getProtocol()))
		{
			OutputStream output = null;
			driverFile = new File("surenpi.com." + new File(url.getFile()).getName());
			if(driverFile.exists())
			{
				return driverFile.getAbsolutePath();
			}
			
			try(InputStream inputStream = url.openStream())
			{
				output = new FileOutputStream(driverFile);
				IOUtils.copy(inputStream, output);
			}
			catch (IOException e)
			{
				logger.error("Driver file copy error.", e);
			}
			finally
			{
				IOUtils.closeQuietly(output);
			}
		}
		else
		{
			try {
				driverFile = new File(URLDecoder.decode(url.getFile(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		if(driverFile != null)
		{
			return driverFile.getAbsolutePath();
		}
		else
		{
			return "";
		}
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
	}

	/**
	 * 关闭浏览器引擎
	 */
	public void close()
	{
		if(driver != null)
		{
			driver.quit();
		}
	}

	public WebDriver getDriver()
	{
		return driver;
	}

	public String getDriverStr()
	{
		return driverStr;
	}

	public void setDriverStr(String driverStr)
	{
		this.driverStr = driverStr;
	}

	public long getTimeout()
	{
		return timeout;
	}

	public void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}

	public boolean isFullScreen()
	{
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen)
	{
		this.fullScreen = fullScreen;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}
}
