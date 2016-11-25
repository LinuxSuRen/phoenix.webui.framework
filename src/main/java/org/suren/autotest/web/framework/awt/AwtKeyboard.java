/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.awt;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.suren.autotest.web.framework.core.Keyboard;

/**
 * @author suren
 * @date 2016年11月25日 上午8:13:40
 */
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
