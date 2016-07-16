package org.suren.autotest.web.framework.selenium.strategy;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.suren.autotest.web.framework.core.ElementSearchStrategy;
import org.suren.autotest.web.framework.core.ui.Element;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

public class ZoneSearchStrategy implements ElementSearchStrategy<WebElement> {

	@Autowired
	private SeleniumEngine engine;
	
	public WebElement search(Element element) {
		// TODO Auto-generated method stub
		return null;
	}

}
