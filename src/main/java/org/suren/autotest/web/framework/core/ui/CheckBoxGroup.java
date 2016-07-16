/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ClickAble;

/**
 * @author suren
 * @date Jul 16, 2016 9:11:49 PM
 */
@Component
public class CheckBoxGroup extends AbstractElement
{
	@Autowired
	private ClickAble clickAble;
	
	public boolean selectByText(String text)
	{
		return true;
	}

	/* (non-Javadoc)
	 * @see org.suren.autotest.web.framework.core.ui.AbstractElement#isEnabled()
	 */
	@Override
	public boolean isEnabled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.suren.autotest.web.framework.core.ui.AbstractElement#isHidden()
	 */
	@Override
	public boolean isHidden()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
