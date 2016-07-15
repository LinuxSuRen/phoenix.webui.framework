/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;

/**
 * 
 * 此处填写类简介
 * <p>
 * 此处填写类说明
 * </p>
 * @author sunyp
 * @since jdk1.6
 * 2016年6月24日
 *  
 */
@Component
public class LogoutPage {
	private Button logoutButtton;

	public Button getLogoutButtton() {
		return logoutButtton;
	}

	public void setLogoutButtton(Button logoutButtton) {
		this.logoutButtton = logoutButtton;
	}
}
