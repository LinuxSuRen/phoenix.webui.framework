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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;

/**
 * @author suren
 * 
 */
@Component
public class LoginPage extends Page {
	@Autowired
	private Button loginBut;
	@Autowired
	private Text phone;
	@Autowired
	private Text password;
	/**
	* getter and setter methods zone
	*/
	public Button getLoginBut()
	{
		return loginBut;
	}
	public void setLoginBut(Button loginBut)
	{
		this.loginBut = loginBut;
	}
	public Text getPhone()
	{
		return phone;
	}
	public void setPhone(Text phone)
	{
		this.phone = phone;
	}
	public Text getPassword()
	{
		return password;
	}
	public void setPassword(Text password)
	{
		this.password = password;
	}
}
