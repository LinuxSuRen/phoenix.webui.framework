/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.page.Page;

/**
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT
 * 2016年7月1日
 */
@Component
public class SystemSelectPage extends Page {

	@Autowired
	private Button sysButton;

	public Button getSysButton() {
		return sysButton;
	}

	public void setSysButton(Button sysButton) {
		this.sysButton = sysButton;
	}
}
