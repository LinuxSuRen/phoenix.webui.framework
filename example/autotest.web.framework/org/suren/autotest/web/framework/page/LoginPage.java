package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.page.Page;

/**
 * @author suren
 * 登录页面
 */
@Component
public class LoginPage extends Page {
	@Autowired
	private Button gender;
	/**
	* getter and setter methods zone
	*/
	public Button getGender()
	{
		return gender;
	}
	public void setGender(Button gender)
	{
		this.gender = gender;
	}
}
