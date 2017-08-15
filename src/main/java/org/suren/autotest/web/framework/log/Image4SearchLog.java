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
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

import com.surenpi.autotest.utils.AnimatedGifEncoder;

/**
 * 添加搜索时的图片记录
 * @author suren
 * @since 2016年8月4日 上午8:13:56
 */
@Component
@Aspect
@Configuration
public class Image4SearchLog
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Image4SearchLog.class);
	
	@Autowired
	private SeleniumEngine engine;
	@Autowired
	private ElementMark elementMark;
	
	private Properties imagePro = new Properties();
	
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
					imagePro.load(imgCfgStream);
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
			outputDir = new File(imagePro.getProperty(LoggerConstants.IMG_LOG_DIR,
					System.getProperty("java.io.tmpdir"))); 
			if(!outputDir.isDirectory())
			{
				if(!outputDir.mkdirs())
				{
					LOGGER.error(String.format("Can not create img dir [%s].", outputDir));
					
					outputDir = new File(System.getProperty("java.io.tmpdir"));
				}
			}
			
			engine.setProgressId(LoggerConstants.IMG_LOG_DIR, outputDir.getAbsolutePath());
		}
		
		return outputDir;
	}
	
	@PostConstruct
	public void init()
	{
		outputDir = getOutputFile(); 
		Map<Object, Object> engineConfig = engine.getEngineConfig();
		long currentTime = System.currentTimeMillis();
		
		String progressIdentify = (String) engineConfig.getOrDefault(
				LoggerConstants.PROGRESS_IDENTIFY, String.valueOf(currentTime));
		String applicationIdentify = (String) engineConfig.getOrDefault(LoggerConstants.APP_IDENTIFY,
				"phoenix");
		
		outputDir = new File(outputDir, applicationIdentify);
		outputDir.mkdirs();
		outputDir = new File(outputDir, progressIdentify);
		outputDir.mkdirs();
		
		String gifPath = new File(outputDir, currentTime + ".gif").getAbsolutePath();
		
		engine.setProgressId(LoggerConstants.APP_IDENTIFY, applicationIdentify);
		engine.setProgressId(LoggerConstants.PROGRESS_IDENTIFY, progressIdentify);
		engine.setProgressId(LoggerConstants.GIF_CURRENT_PATH, gifPath);
		
		animatedGifEncoder = new AnimatedGifEncoder();
		animatedGifEncoder.start(gifPath);
		animatedGifEncoder.setDelay(800);
		animatedGifEncoder.setRepeat(0);
	}
	
	@Before("within(com.surenpi.autotest.webui.core.ElementSearchStrategy)")
	public void search()
	{
		System.out.println("sdds");
	}

	@Around("execution(* com.surenpi.autotest.webui.core.ElementSearchStrategy.search*(..))")
	public Object imageLog(ProceedingJoinPoint joinPoint) throws Throwable
	{
		Object[] args = joinPoint.getArgs();
		
		Object res = null;
		Throwable ea = null;
		try
		{
			res = joinPoint.proceed(args);
		}
		catch(Throwable e)
		{
			ea = e;
			
			throw e;
		}
		finally
		{
			capture(res, ea);
		}
		
		return res;
	}
	
	/**
	 * 截图捕捉
	 * @param ele
	 * @throws IOException
	 */
	private void capture(Object ele, Throwable e) throws IOException
	{
		if(ele == null)
		{
			return;
		}
		
		WebDriver driver = engine.getDriver();
		if(ele instanceof WebElement && driver instanceof TakesScreenshot)
		{
			TakesScreenshot shot = (TakesScreenshot) driver;
			
			File file = shot.getScreenshotAs(OutputType.FILE);
			BufferedImage bufImg = ImageIO.read(file);
			
			try
			{
				elementMark.mark((WebElement) ele, file);
			}
			catch(IOException ioEx)
			{
				ioEx.printStackTrace();
			}
			
			long currentTime =  System.currentTimeMillis();
			String fileName = null;
			if(e == null)
			{
				fileName = currentTime + ".png";
			}
			else
			{
				fileName = "ex_" + currentTime + ".png";
			}
			
			File elementSearchImageFile = new File(outputDir, fileName);
			try(OutputStream output = new FileOutputStream(elementSearchImageFile))
			{
				ImageIO.write(bufImg, "gif", output);
				elementSearchImageFileList.add(elementSearchImageFile);
				animatedGifEncoder.addFrame(bufImg);
			}
		}
	}

	@Bean
	public ElementMark createElementMark()
	{
		boolean mark = Boolean.valueOf(imagePro.getProperty("element.mark", "true"));
		if(mark)
		{
			return new TargetElementMark();
		}
		else
		{
			return new BlankElementMark();
		}
	}
	
	@PreDestroy
	public void finish()
	{
		animatedGifEncoder.finish();
	}
}
