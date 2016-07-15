/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;

/**
 * 企业基本信息-安全生产许可证
 * 此处填写类简介
 * <p>
 * 此处填写类说明
 * </p>
 * @author sunyp
 * @since jdk1.6
 * 2016年7月5日
 *  
 */
@Component
public class SafetyProductPermitPage extends Page{
	
	/** 许可证编号 */
	@Autowired
	private Text PermitNo;
	/** 有效期始 */
	@Autowired
	private Text DateStart;
	/** 有效期止 */
	@Autowired
	private Text DateEnd;
	/** 许可范围 */
	@Autowired
	private Text ValidRange;
	public Text getPermitNo() {
		return PermitNo;
	}
	public void setPermitNo(Text permitNo) {
		PermitNo = permitNo;
	}
	public Text getDateStart() {
		return DateStart;
	}
	public void setDateStart(Text dateStart) {
		DateStart = dateStart;
	}
	public Text getDateEnd() {
		return DateEnd;
	}
	public void setDateEnd(Text dateEnd) {
		DateEnd = dateEnd;
	}
	public Text getValidRange() {
		return ValidRange;
	}
	public void setValidRange(Text validRange) {
		ValidRange = validRange;
	}

}
