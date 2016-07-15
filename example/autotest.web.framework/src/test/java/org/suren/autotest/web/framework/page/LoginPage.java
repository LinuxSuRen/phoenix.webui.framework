/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;

/**
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT
 * 2016年7月1日
 */
@Component
public class LoginPage extends Page {
	@Autowired
	private Text loginNameText;
	@Autowired
	private Text passwdText;
	@Autowired
	private Text validText;
	@Autowired
	private Button loginButton;
	
	public Text getLoginNameText() {
		return loginNameText;
	}
	public void setLoginNameText(Text loginNameText) {
		this.loginNameText = loginNameText;
	}

	public Text getPasswdText() {
		return passwdText;
	}
	public void setPasswdText(Text passwdText) {
		this.passwdText = passwdText;
	}
	
	public Text getValidText() {
		return validText;
	}
	public void setValidText(Text validText) {
		this.validText = validText;
	}
	
	public Button getLoginButton() {
		return loginButton;
	}
	public void setLoginButton(Button loginButton) {
		this.loginButton = loginButton;
	}
}
