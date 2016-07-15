/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ClickAble;
import org.suren.autotest.web.framework.core.action.ValueEditor;

/**
 * 文本框封装
 * @author zhaoxj
 * @since jdk1.6
 * 2016年6月29日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Text extends AbstractElement {
	private String value;
	
	@Autowired
	private ValueEditor valueEditor;
	@Autowired
	private ClickAble clickAble;
	
	public Text(){}
	
	public Text(String value) {
		this.value = value;
	}
	
	/**
	 * 自动填充数据，不用关系数据来源
	 */
	public void fillValue() {
		valueEditor.setValue(this, value);
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ValueEditor getValueEditor() {
		return valueEditor;
	}
	public void setValueEditor(ValueEditor valueEditor) {
		this.valueEditor = valueEditor;
	}
	public ClickAble getClickAble() {
		return clickAble;
	}
	public void setClickAble(ClickAble clickAble) {
		this.clickAble = clickAble;
	}
}
