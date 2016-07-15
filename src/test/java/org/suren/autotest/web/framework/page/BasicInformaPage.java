/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Selector;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;

/**企业基本信息-基本信息
 * @author zhaoxj
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT
 * 2016年7月1日
 */
@Component
public class BasicInformaPage extends Page {
	
	/** 企业名称 */
	@Autowired
	private Text EnterpriseName;
	/** 注册地址-省 */
	@Autowired
	private Selector RegisterAddrP;
	/** 注册地址-市 */
	@Autowired
	private Selector RegisterAddrC;
	/** 注册地址-区 */
	@Autowired
	private Selector RegisterAddrQ;
	/** 注册地址 */
	@Autowired
	private Text RegisterAddr;
	/**注册地址邮编  */
	@Autowired
	private Text RegisterCode;
	/**企业注册时间  */
	@Autowired
	private Button RegisterDate;
	/**开户银行  */
	@Autowired
	private Text Bank;
	/**基本账户号  */
	@Autowired
	private Text BankNum;
	/** 是否为三证合一的营业执照号-是 */
	@Autowired
	private Button OrganizationCodeA;
	/** 是否为三证合一的营业执照号-否 */
	@Autowired
	private Button OrganizationCodeB;
	/**业务方向  */
	@Autowired
	private Button BusinessDirec;
	/** 企业类型 */
	@Autowired
	private Button EnterpriseType;
	/** 业务类别-选择 */
	@Autowired
	private Button BusinessTypeSelect;
	/**业务类别-一级  */
	@Autowired
	private Button BusinessTree1;
	/**业务类别-二级  */
	@Autowired
	private Button BusinessTree2;
	/**业务类别-电梯  */
	@Autowired
	private Button BusinessTreeIco;
	/**保存 */
	@Autowired
	private Button btnSave;	
	public Button getBtnSave() {
		return btnSave;
	}
	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}
	public Text getEnterpriseName() {
		return EnterpriseName;
	}
	public void setEnterpriseName(Text enterpriseName) {
		EnterpriseName = enterpriseName;
	}
	public Selector getRegisterAddrP() {
		return RegisterAddrP;
	}
	public void setRegisterAddrP(Selector registerAddrP) {
		RegisterAddrP = registerAddrP;
	}
	public Selector getRegisterAddrC() {
		return RegisterAddrC;
	}
	public void setRegisterAddrC(Selector registerAddrC) {
		RegisterAddrC = registerAddrC;
	}
	public Selector getRegisterAddrQ() {
		return RegisterAddrQ;
	}
	public void setRegisterAddrQ(Selector registerAddrQ) {
		RegisterAddrQ = registerAddrQ;
	}
	public Text getRegisterAddr() {
		return RegisterAddr;
	}
	public void setRegisterAddr(Text registerAddr) {
		RegisterAddr = registerAddr;
	}
	public Text getRegisterCode() {
		return RegisterCode;
	}
	public void setRegisterCode(Text registerCode) {
		RegisterCode = registerCode;
	}
	public Button getRegisterDate() {
		return RegisterDate;
	}
	public void setRegisterDate(Button registerDate) {
		RegisterDate = registerDate;
	}
	public Text getBank() {
		return Bank;
	}
	public void setBank(Text bank) {
		Bank = bank;
	}
	public Text getBankNum() {
		return BankNum;
	}
	public void setBankNum(Text bankNum) {
		BankNum = bankNum;
	}
	public Button getOrganizationCodeA() {
		return OrganizationCodeA;
	}
	public void setOrganizationCodeA(Button organizationCodeA) {
		OrganizationCodeA = organizationCodeA;
	}
	public Button getOrganizationCodeB() {
		return OrganizationCodeB;
	}
	public void setOrganizationCodeB(Button organizationCodeB) {
		OrganizationCodeB = organizationCodeB;
	}
	public Button getBusinessDirec() {
		return BusinessDirec;
	}
	public void setBusinessDirec(Button businessDirec) {
		BusinessDirec = businessDirec;
	}
	public Button getEnterpriseType() {
		return EnterpriseType;
	}
	public void setEnterpriseType(Button enterpriseType) {
		EnterpriseType = enterpriseType;
	}
	public Button getBusinessTypeSelect() {
		return BusinessTypeSelect;
	}
	public void setBusinessTypeSelect(Button businessTypeSelect) {
		BusinessTypeSelect = businessTypeSelect;
	}
	public Button getBusinessTree1() {
		return BusinessTree1;
	}
	public void setBusinessTree1(Button businessTree1) {
		BusinessTree1 = businessTree1;
	}
	public Button getBusinessTree2() {
		return BusinessTree2;
	}
	public void setBusinessTree2(Button businessTree2) {
		BusinessTree2 = businessTree2;
	}
	public  Button getBusinessTreeIco() {
		return BusinessTreeIco;
	}
	public  void setBusinessTreeIco(Button businessTreeIco) {
		BusinessTreeIco = businessTreeIco;
	}


}
