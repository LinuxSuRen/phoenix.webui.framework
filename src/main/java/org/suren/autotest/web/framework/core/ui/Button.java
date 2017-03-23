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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.action.ClickAble;
import org.suren.autotest.web.framework.core.action.HoverAble;
import org.suren.autotest.web.framework.core.action.SequenceAble;
import org.suren.autotest.web.framework.util.StringUtils;

/**
 * 代表HTML页面中的按钮，即在xml配置文件中type值为button的元素
 * 
 * @author suren
 * @since jdk1.6 2016年6月29日
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Button extends AbstractElement
{
	@Autowired
	private ClickAble clickAble;
	@Autowired
	private HoverAble hoverAble;
	@Autowired
	private List<SequenceAble> sequenceAbleList;

	/**
	 * 触发单击事件
	 */
	public void click()
	{
		clickAble.click(this);
	}
	
	/**
	 * 触发双击事件
	 */
	public void dbClick()
	{
		clickAble.dbClick(this);
	}
	
	/**
	 * 鼠标悬停
	 */
	public void hover()
	{
		hoverAble.hover(this);
	}
	
	public void sequenceOperation()
	{
		List<String> actions = new ArrayList<String>();
		
		String seqOper = (String) getData("SEQ_OPER");
		String seqOperName = getDataStr("SEQ_OPER_NAME");
		
		actions.addAll(Arrays.asList(seqOper.split(",")));
		
		clickAble.click(this);
		
		for(SequenceAble sequenceAble : sequenceAbleList)
		{
			String tagetSeqName = sequenceAble.getName();
			if(StringUtils.isBlank(tagetSeqName))
			{
				throw new RuntimeException(String.format("The SequenceAble'name is blank! Class is %s.",
						sequenceAble.getClass()));
			}
			
			if(tagetSeqName.equals(seqOperName))
			{
				sequenceAble.perform(this, actions);
				break;
			}
		}
	}

	@Override
	public boolean isEnabled()
	{
		return clickAble.isEnabled(this);
	}

	@Override
	public boolean isHidden()
	{
		return clickAble.isHidden(this);
	}

	public ClickAble getClickAble()
	{
		return clickAble;
	}

	/** setter and getter */
	public void setClickAble(ClickAble clickAble)
	{
		this.clickAble = clickAble;
	}

	public HoverAble getHoverAble()
	{
		return hoverAble;
	}

	public void setHoverAble(HoverAble hoverAble)
	{
		this.hoverAble = hoverAble;
	}
}
