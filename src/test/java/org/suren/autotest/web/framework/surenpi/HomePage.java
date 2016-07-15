package org.suren.autotest.web.framework.surenpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.page.Page;

@Component("surenpi.home.page")
public class HomePage extends Page {
	@Autowired
	private Button homeBut;

	public Button getHomeBut() {
		return homeBut;
	}

	public void setHomeBut(Button homeBut) {
		this.homeBut = homeBut;
	}
}
