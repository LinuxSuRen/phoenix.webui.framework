/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.log;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

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
import org.openqa.selenium.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.util.AnimatedGifEncoder;

/**
 * @author suren
 * @date 2016年8月4日 上午8:13:56
 */
@Component
@Aspect
public class Image4SearchLog
{
	@Autowired
	private SeleniumEngine engine;
	
	private File outputDir;
	private List<File> elementSearchImageFileList = new ArrayList<File>();
	
	private AnimatedGifEncoder animatedGifEncoder;
	
	@PostConstruct
	public void init()
	{
		outputDir = new File("d:/ElementSearch/image");
		if(!outputDir.isDirectory())
		{
			outputDir.mkdirs(); // TODO这里没有进行是否创建成功的判断
		}
		
		animatedGifEncoder = new AnimatedGifEncoder();
		animatedGifEncoder.start(new File(outputDir, System.currentTimeMillis() + ".gif").getAbsolutePath());
		animatedGifEncoder.setDelay(800);
		animatedGifEncoder.setRepeat(0);
	}

	@Around("execution(* org.suren.autotest.web.framework.core.ElementSearchStrategy.search*(..))")
	public Object hello(ProceedingJoinPoint joinPoint)
	{
		Object[] args = joinPoint.getArgs();
		
		Object res = null;
		try
		{
			res = joinPoint.proceed(args);
			
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
				
				OutputStream output = null;
				
				try
				{
					File elementSearchImageFile = new File(outputDir, file.getName());
					output = new FileOutputStream(elementSearchImageFile);
					
					ImageIO.write(bufImg, "gif", output);
					elementSearchImageFileList.add(elementSearchImageFile);
					animatedGifEncoder.addFrame(bufImg);
				}
				finally
				{
					IOUtils.closeQuietly(output);
				}
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		
		return res;
	}
	
	@PreDestroy
	public void finish()
	{
		animatedGifEncoder.finish();
	}
}
