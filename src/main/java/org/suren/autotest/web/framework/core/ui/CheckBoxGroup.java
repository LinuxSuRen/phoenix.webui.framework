/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.CheckAble;

/**
 * 复选框组，代表一组HTML中的复选框
 * @author suren
 * @date Jul 16, 2016 9:11:49 PM
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheckBoxGroup extends AbstractElement
{
	@Autowired
	private CheckAble checkAble;
	
	/**
	 * 根据文本选择
	 * @param text
	 * @return
	 */
	public boolean selectByText(String text)
	{
		checkAble.checkByText(this, text);
		
		return true;
	}
	
	/**
	 * @see #selectByText(String)
	 * @param texts
	 * @return
	 */
	public int selectByTextArray(String ...texts)
	{
		int count = 0;
		if(texts == null)
		{
			return count;
		}
		
		for(String text : texts)
		{
			if(selectByText(text))
			{
				count++;
			}
		}
		
		return count;
	}

	@Override
	public boolean isEnabled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHidden()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
