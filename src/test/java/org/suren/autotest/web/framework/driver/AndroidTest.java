///*
// * Copyright 2002-2007 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package org.suren.autotest.web.framework.driver;
//
//import io.appium.java_client.AppiumDriver;
//
//import java.io.File;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import org.junit.Test;
//import org.openqa.selenium.remote.CapabilityType;
//import org.openqa.selenium.remote.DesiredCapabilities;
//
///**
// * @author suren
// * @date 2017年5月20日 下午5:42:00
// */
//public class AndroidTest
//{
//	@Test
//	public void test() throws MalformedURLException
//	{
//        File appFile = new File("D:/suren.apk");
//        
//        //设置自动化相关参数
//        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability(CapabilityType.BROWSER_NAME, "");
//        capabilities.setCapability("platformName", "Android");
//        capabilities.setCapability("deviceName", "750BBKP23E75"); //该参数可通过adb命令来获取
//        
//        //设置安卓系统版本
//        capabilities.setCapability("platformVersion", "4.3");
//        //设置apk路径
//        capabilities.setCapability("app", appFile.getAbsolutePath()); 
//        
//        //设置app的主包名和主类名
//        capabilities.setCapability("appPackage", "com.example.android.contactmanager");
//        capabilities.setCapability("appActivity", ".ContactManager");
//        
//        //初始化
//        AppiumDriver driver = new AppiumDriver(
//                new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//        
//        System.out.println("started");
//        
//        driver.quit();
//	}
//}
