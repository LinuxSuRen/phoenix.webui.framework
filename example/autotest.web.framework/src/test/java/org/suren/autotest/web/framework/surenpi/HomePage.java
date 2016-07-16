package org.suren.autotest.web.framework.surenpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.page.Page;

@Component("surenpi.home.page")
public class HomePage extends Page {
	@Autowired
	private Button homeBut;
	@Autowired
	private Selector archivesSelector;
	@Autowired
	private Button nextBut;

	public Button getHomeBut() {
		return homeBut;
	}

	public void setHomeBut(Button homeBut) {
		this.homeBut = homeBut;
	}

	public Selector getArchivesSelector() {
		return archivesSelector;
	}

	public void setArchivesSelector(Selector archivesSelector) {
		this.archivesSelector = archivesSelector;
	}

	public Button getNextBut() {
		return nextBut;
	}

	public void setNextBut(Button nextBut) {
		this.nextBut = nextBut;
	}
}
