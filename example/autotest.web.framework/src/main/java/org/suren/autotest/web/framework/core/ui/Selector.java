/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
*/
package org.suren.autotest.web.framework.core.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.SelectAble;

/**
 * 下拉列表
 * 
 * @author suren
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT 2016年7月1日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Selector extends AbstractElement
{
	private String text;
	
	@Autowired
	private SelectAble selectAble;

	/**
	 * 根据下拉列表的文本值来选择
	 * 
	 * @param text
	 * @return
	 */
	public boolean selectByText(String text)
	{
		return selectAble.selectByText(this, text);
	}
	
	public boolean selectByText()
	{
		return selectByText(this.text);
	}

	public SelectAble getSelectAble()
	{
		return selectAble;
	}

	public void setSelectAble(SelectAble selectAble)
	{
		this.selectAble = selectAble;
	}

	@Override
	public boolean isEnabled()
	{
		return selectAble.isEnabled(this);
	}

	@Override
	public boolean isHidden()
	{
		return selectAble.isHidden(this);
	}

	/**
	 * @return the text
	 */
	public String getText()
	{
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text)
	{
		this.text = text;
	}
}
