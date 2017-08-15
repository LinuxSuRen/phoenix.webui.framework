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

package org.suren.autotest.web.framework.selenium.action;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

import com.surenpi.autotest.webui.action.KeyBoardAble;
import com.surenpi.autotest.webui.ui.Element;

/**
 * 键盘动作实现
 * @author <a href="http://surenpi.com">suren</a>
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
		
		new Actions(driver).keyDown(Keys.ENTER).perform();
	}

}
