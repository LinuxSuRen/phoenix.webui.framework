/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ValueEditor;
import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author zhaoxj
 * @since jdk1.6
 * 2016年6月29日
 */
@Component
public class SeleniumValueEditor implements ValueEditor {

	@Autowired
	private SeleniumEngine engine;

	public Object getValue(Element ele) {
		return engine.getDriver().findElement(By.id(ele.getId())).getText();
	}

	public void setValue(Element ele, Object value) {
		if(value == null) {
			throw new IllegalArgumentException("value can not be null.");
		}
		
		engine.getDriver().findElement(By.id(ele.getId())).sendKeys(value.toString());
	}

}
