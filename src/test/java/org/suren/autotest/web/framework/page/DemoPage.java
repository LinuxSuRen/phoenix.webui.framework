/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.page;

import org.suren.autotest.web.framework.selenium.WebPage;

import com.surenpi.autotest.webui.ui.Button;
import com.surenpi.autotest.webui.ui.Text;

/**
 * @author suren
 * @date 2017年5月10日 下午8:10:06
 */
public class DemoPage extends WebPage
{
	private Text userName;
	private Text password;
	private Button loginBut;
	/**
	 * @return the userName
	 */
	public Text getUserName()
	{
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(Text userName)
	{
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public Text getPassword()
	{
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(Text password)
	{
		this.password = password;
	}
	/**
	 * @return the loginBut
	 */
	public Button getLoginBut()
	{
		return loginBut;
	}
	/**
	 * @param loginBut the loginBut to set
	 */
	public void setLoginBut(Button loginBut)
	{
		this.loginBut = loginBut;
	}
}
