/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.suren.autotest.web.framework.core.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.RandomSelectAble;
import org.suren.autotest.web.framework.core.action.SelectAble;

/**
 * 下拉列表，支持根据文本和序号来选择
 * 
 * @author suren
 * @since jdk1.6
 * @since 2016年7月1日
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
	@Autowired
	private RandomSelectAble randomSelectAble;

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
	 * 随机选择一个选项
	 * @return 选中的元素对象
	 */
	public Object randomSelect()
	{
		return randomSelectAble.randomSelect(this);
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
