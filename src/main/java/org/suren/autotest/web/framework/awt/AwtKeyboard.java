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

package org.suren.autotest.web.framework.awt;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.springframework.stereotype.Component;

import com.surenpi.autotest.webui.core.Keyboard;

/**
 * @author suren
 * @date 2016年11月25日 上午8:13:40
 */
@Component
public class AwtKeyboard implements Keyboard
{

	@Override
	public void enter()
	{
		try
		{
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void space()
	{
		try
		{
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_SPACE);
			robot.keyRelease(KeyEvent.VK_SPACE);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

}
