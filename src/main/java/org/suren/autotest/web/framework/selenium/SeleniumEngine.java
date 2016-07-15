/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;

/**
 * @author zhaoxj
 * @since jdk1.6
 * 2016年6月29日
 */
@Component
public class SeleniumEngine {
	private String driverStr;
	private WebDriver driver;
	
	public SeleniumEngine() {
//		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
//	        capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
//	        
//        File file = new File("d:/IEDriverServer.exe");  
//        System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
//        
//		driver = new InternetExplorerDriver(capability);
//		driver = new FirefoxDriver();
		
		System.setProperty("webdriver.chrome.driver", "/usr/lib/chromium-browser/chromedriver");
		driver = new ChromeDriver();
	}
	
	public void openUrl(String url) {
		driver.get(url);
	}
	
	public void close() {
		driver.close();
	}

	public WebDriver getDriver() {
		return driver;
	}

	public String getDriverStr() {
		return driverStr;
	}

	public void setDriverStr(String driverStr) {
		this.driverStr = driverStr;
	}
}
