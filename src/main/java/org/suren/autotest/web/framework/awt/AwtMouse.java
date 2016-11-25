/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.awt;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.Mouse;

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
