/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium.action;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.suren.autotest.web.framework.core.action.SequenceAble;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.util.ThreadUtil;

/**
 * 利用Selenium来实现序列操作
 * @author suren
 * @date 2017年1月11日 下午4:56:58
 */
@Component
public class SeleniumSequenceOperation implements SequenceAble
{

	@Autowired
	private SeleniumEngine engine;
	
	@Override
	public void perform(Element element, List<String> actions)
	{
		if(CollectionUtils.isEmpty(actions) || actions.size() <= 1)
		{
			throw new RuntimeException("Error format.");
		}
		
		String parentXPath = actions.get(0);
		
		String xpath = parentXPath;
		WebDriver driver = engine.getDriver();
		
		WebElement parentEle = driver.findElement(By.xpath(xpath));
		
		int index = 1;
		for(; index < actions.size() - 1; index++)
		{
			xpath = String.format("//span[contains(text(),'%s')]", actions.get(index));
			
			System.out.println(xpath);
			parentEle = parentEle.findElement(By.xpath(xpath));
			String id = parentEle.getAttribute("id");
			System.out.println(id);
			id = id.replace("span", "switch");
			System.out.println(id);
			parentEle = driver.findElement(By.id(id));
			parentEle.click();
			
			ThreadUtil.silentSleep(2000);
		}
		
		xpath = String.format("//span[contains(text(),'%s')]", actions.get(index));
		System.out.println(xpath);
		WebDriverWait wait = new WebDriverWait(engine.getDriver(), 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		parentEle.findElement(By.xpath(xpath)).click();
	}

}
