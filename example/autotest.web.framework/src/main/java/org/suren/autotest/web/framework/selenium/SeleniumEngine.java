/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

/**
 * @author zhaoxj
 * @since jdk1.6
 * 2016年6月29日
 */
@Component
public class SeleniumEngine {
	private WebDriver driver;
	
	public SeleniumEngine() {
		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
	        capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
	        
        File file = new File("d:/IEDriverServer.exe");  
        System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
        
		driver = new InternetExplorerDriver(capability); 
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
}
