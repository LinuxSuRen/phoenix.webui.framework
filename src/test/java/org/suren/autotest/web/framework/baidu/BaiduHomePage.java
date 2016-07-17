/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.baidu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;

/**
 * @author suren
 * @date Jul 16, 2016 9:38:27 PM
 */
@Component
public class BaiduHomePage extends Page
{
	@Autowired
	private Button toLoginBut;
	
	@Autowired
	private Text userNameText;
	@Autowired
	private Text passwdText;
	@Autowired
	private Button loginBut;
	public Button getToLoginBut()
	{
		return toLoginBut;
	}
	public void setToLoginBut(Button toLoginBut)
	{
		this.toLoginBut = toLoginBut;
	}
	public Text getUserNameText()
	{
		return userNameText;
	}
	public void setUserNameText(Text userNameText)
	{
		this.userNameText = userNameText;
	}
	public Text getPasswdText()
	{
		return passwdText;
	}
	public void setPasswdText(Text passwdText)
	{
		this.passwdText = passwdText;
	}
	public Button getLoginBut()
	{
		return loginBut;
	}
	public void setLoginBut(Button loginBut)
	{
		this.loginBut = loginBut;
	}
}