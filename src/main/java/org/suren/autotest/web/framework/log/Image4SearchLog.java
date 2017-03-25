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

package org.suren.autotest.web.framework.log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.util.AnimatedGifEncoder;

/**
 * 添加搜索时的图片记录
 * @author suren
 * @date 2016年8月4日 上午8:13:56
 */
@Component
@Aspect
@Scope(value = "autotest", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Image4SearchLog
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Image4SearchLog.class);
	
	@Autowired
	private SeleniumEngine engine;
	
	private Properties pro = new Properties();
	
	private String progressIdentify;
	private File outputDir;
	private List<File> elementSearchImageFileList = new ArrayList<File>();
	
	private AnimatedGifEncoder animatedGifEncoder;
	
	public Image4SearchLog()
	{
		try
		{
			Enumeration<URL> urls = Image4SearchLog.class.getClassLoader().getResources(
					LoggerConstants.IMG_LOG_CONF_FILE_NAME);
			while(urls.hasMoreElements())
			{
				URL url = urls.nextElement();
				
				try(InputStream imgCfgStream = url.openStream())
				{
					pro.load(imgCfgStream);
				}
			}
		}
		catch (IOException e)
		{
			LOGGER.error("Image4Search config file finding error.", e);
		}
	}
	
	public File getOutputFile()
	{
		if(outputDir == null)
		{
			outputDir = new File(pro.getProperty(LoggerConstants.IMG_LOG_DIR, System.getProperty("java.io.tmpdir"))); 
			if(!outputDir.isDirectory())
			{
				if(!outputDir.mkdirs())
				{
					LOGGER.error(String.format("Can not create img dir [%s].", outputDir));
					
					outputDir = new File(System.getProperty("java.io.tmpdir"));
					pro.setProperty(LoggerConstants.IMG_LOG_DIR, outputDir.getAbsolutePath());
				}
			}
		}
		
		return outputDir;
	}
	
	@PostConstruct
	public void init()
	{
		outputDir = getOutputFile(); 
		
		progressIdentify = (String) engine.getEngineConfig().get("progress_identify");
		
		animatedGifEncoder = new AnimatedGifEncoder();
		animatedGifEncoder.start(new File(outputDir, progressIdentify + ".gif").getAbsolutePath());
		animatedGifEncoder.setDelay(800);
		animatedGifEncoder.setRepeat(0);
	}

	@Around("execution(* org.suren.autotest.web.framework.core.ElementSearchStrategy.search*(..))")
	public Object hello(ProceedingJoinPoint joinPoint) throws Throwable
	{
		Object[] args = joinPoint.getArgs();
		
		Object res = joinPoint.proceed(args);
		if(res instanceof WebElement)
		{
			WebDriver driver = engine.getDriver();
			TakesScreenshot shot = (TakesScreenshot) driver;
			
			File file = shot.getScreenshotAs(OutputType.FILE);
			BufferedImage bufImg = ImageIO.read(file);
			
			try
			{
				WebElement webEle = (WebElement) res;
				Point loc = webEle.getLocation();
				Dimension size = webEle.getSize();
				
				Graphics2D g = bufImg.createGraphics();
				g.setColor(Color.red);
				g.drawRect(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
			}
			catch(StaleElementReferenceException e)
			{
				//
			}
			
			File elementSearchImageFile = new File(outputDir, progressIdentify + "_" + System.currentTimeMillis() + ".png");
			try(OutputStream output = new FileOutputStream(elementSearchImageFile))
			{
				ImageIO.write(bufImg, "gif", output);
				elementSearchImageFileList.add(elementSearchImageFile);
				animatedGifEncoder.addFrame(bufImg);
			}
		}
		
		return res;
	}
	
	@PreDestroy
	public void finish()
	{
		animatedGifEncoder.finish();
	}
}
