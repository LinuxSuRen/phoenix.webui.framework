package org.suren.autotest.web.framework.selenium.strategy;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

public class CyleSearchStrategy implements ElementSearchStrategy<WebElement> {

	@Autowired
	private SeleniumEngine engine;
	
	public WebElement search(Element element) {
		WebElement webEle = null;
		WebDriver driver = engine.getDriver();
		
		if(StringUtils.isNotBlank(element.getId())) {
			webEle = driver.findElement(By.id(element.getId()));
		} else if(StringUtils.isNotBlank(element.getXPath())) {
			webEle = driver.findElement(By.xpath(element.getXPath()));
		} else if(StringUtils.isNotBlank(element.getLinkText())) {
			webEle = driver.findElement(By.linkText(element.getLinkText()));
		}
		
		return webEle;
	}

}
