/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;

/**
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT
 * 2016年7月1日
 */
@Component
public class HomePage extends Page {

	@Autowired
	private Button gfmButton;

	public Button getGfmButton() {
		return gfmButton;
	}

	public void setGfmButton(Button gfmButton) {
		this.gfmButton = gfmButton;
	}
}