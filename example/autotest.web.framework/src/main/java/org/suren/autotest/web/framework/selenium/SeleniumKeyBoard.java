/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.selenium;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.KeyBoardAble;
import org.suren.autotest.web.framework.core.ui.Element;

/**
 * @author suren
 * @date Jul 23, 2016 6:38:44 PM
 */
@Component
public class SeleniumKeyBoard implements KeyBoardAble
{
	@Autowired
	private SeleniumEngine engine;

	@Override
	public void enter(Element element)
	{
		WebDriver driver = engine.getDriver();
		
//		new Actions(driver).keyDown(element, Keys.ENTER).perform();
	}

}
