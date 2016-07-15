/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.SelectAble;
import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT
 * 2016年7月1日
 */
@Component
public class SeleniumSelect implements SelectAble {

	@Autowired
	private SeleniumEngine engine;

	public boolean selectByText(Element ele, String text) {
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
			Select select = new Select(webEle);
			select.selectByVisibleText(text);
		}
		
		return true;
	}

	public boolean selectByValue(Element ele, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean selectByIndex(Element ele, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
