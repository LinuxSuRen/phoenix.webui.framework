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
import java.awt.event.InputEvent;

import org.springframework.stereotype.Component;

import com.surenpi.autotest.webui.core.Mouse;

/**
 * 使用JRE提供的AWT库来实现鼠标动作
 * @author suren
 * @date 2016年11月25日 上午8:10:36
 */
@Component
public class AwtMouse implements Mouse
{

	@Override
	public void wheel()
	{
		wheel(1);
	}

	@Override
	public void wheel(int num)
	{
		try
		{
			new Robot().mouseWheel(num);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void click()
	{
		try
		{
			Robot robot = new Robot();
			robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void rightClick()
	{
		// TODO Auto-generated method stub

	}

}
