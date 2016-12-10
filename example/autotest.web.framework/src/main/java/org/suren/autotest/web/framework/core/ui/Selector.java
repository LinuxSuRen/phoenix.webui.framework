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
 * 下拉列表，支持根据文本和序号来选择
 * 
 * @author suren
 * @since jdk1.6
 * @since 3.1.1-SNAPSHOT 2016年7月1日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Selector extends AbstractElement
{
	/** 文本 */
	private String text;
	/** 序号 */
	private int index;
	/** 值 */
	private String value;
	
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
	
	/**
	 * 根据数据源中指定的文本来选择
	 * @return
	 */
	public boolean selectByText()
	{
		return selectByText(this.text);
	}
	
	/**
	 * 根据序号（从0开始）来选择
	 * @param index
	 * @return
	 */
	public boolean selectByIndex(int index)
	{
		return selectAble.selectByIndex(this, index);
	}
	
	/**
	 * 根据数据源中指定的序号（从0开始）来选择
	 * @return
	 */
	public boolean selectByIndex()
	{
		return selectByIndex(index);
	}
	
	/**
	 * 根据指定的值来选择
	 * @param value
	 * @return
	 */
	public boolean selectByValue(String value)
	{
		return selectAble.selectByValue(this, value);
	}
	
	/**
	 * 根据数据源中指定的值来选择
	 * @return
	 */
	public boolean selectByValue()
	{
		return selectByValue(value);
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

	/** getter and setter methods */
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

	/**
	 * @return the index
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}
}
