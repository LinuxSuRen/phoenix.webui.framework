/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ClickAble;
import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author zhaoxj
 * @since jdk1.6
 * 2016年6月29日
 */
@Component
public class SeleniumClick implements ClickAble {

	@Autowired
	private SeleniumEngine engine;
	
	public void click(Element ele) {
		WebElement webEle = null;
		WebDriver driver = engine.getDriver();
		
		if(ele.getId() != null && !"".equals(ele.getId())) {
			webEle = driver.findElement(By.id(ele.getId()));
		} else if(ele.getXPath() != null && !"".equals(ele.getXPath())) {
			webEle = driver.findElement(By.xpath(ele.getXPath()));
		} else if(ele.getLinkText() != null && !"".equals(ele.getLinkText())) {
			webEle = driver.findElement(By.linkText(ele.getLinkText()));
		}
		
		if(webEle != null) {
			webEle.click();
		}
	}

	public void dbClick(Element ele) {
		engine.getDriver().findElement(By.id(ele.getId())).click();
	}

}
