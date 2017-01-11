/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
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
	private SequenceAble sequenceAble;

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
		
		actions.addAll(Arrays.asList(seqOper.split(",")));
		
		clickAble.click(this);
		sequenceAble.perform(this, actions);
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
