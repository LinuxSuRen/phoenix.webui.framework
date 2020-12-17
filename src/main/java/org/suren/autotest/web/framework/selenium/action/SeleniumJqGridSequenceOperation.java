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

package org.suren.autotest.web.framework.selenium.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.surenpi.autotest.webui.action.SequenceAble;
import com.surenpi.autotest.webui.core.LocatorUtil;
import com.surenpi.autotest.webui.ui.AbstractElement;
import com.surenpi.autotest.webui.ui.Button;
import com.surenpi.autotest.webui.ui.Element;
import com.surenpi.autotest.webui.ui.Text;

/**
 * jqGrid组件的选择
 * @author linuxsuren
 */
@Component
public class SeleniumJqGridSequenceOperation implements SequenceAble
{
	private static final Logger logger = LoggerFactory.getLogger(SeleniumJqGridSequenceOperation.class);
	
	/** 关键字搜索定位方法 */
	public static final String KW_SEARCH_BY = "keyword_search_by";
	public static final String KW_SEARCH_INFO = "keyword_search_info";
	public static final String SEARCH_BUT_BY = "search_button_by";
	public static final String SEARCH_BUT_INFO = "search_button_info";
	public static final String OPER_BUT_BY = "operation_button_by";
	public static final String OPER_BUT_INFO = "operation_button_info";
	
	private Text searchText;
	private Button searchBut;
	private Button operationBut;

	@Override
	public void perform(Element element, List<String> actions)
	{
		if(!(element instanceof AbstractElement))
		{
			return;
		}
		
		AbstractElement absEle = ((AbstractElement) element);
		String keyword = ((Text) element).getValue();
		
		buildSearchText(absEle);
		searchText.fillValue(keyword);
		
		buildSearchButton(absEle);
		searchBut.click();
		
		buildOperationBut(absEle);
		operationBut.click();
	}

	/**
	 * @param absEle
	 * @return
	 */
	private Text buildSearchText(AbstractElement absEle)
	{
		Map<String, String> map = Collections.singletonMap(
				absEle.getDataStr(KW_SEARCH_BY), absEle.getDataStr(KW_SEARCH_INFO));
		map.put("strategy", "priority");
		
		LocatorUtil.setLocator(map, searchText);
		
		return searchText;
	}

	/**
	 * @param absEle
	 */
	private Button buildSearchButton(AbstractElement absEle)
	{
		Map<String, String> map = Collections.singletonMap(
				absEle.getDataStr(SEARCH_BUT_BY), absEle.getDataStr(SEARCH_BUT_INFO));
		map.put("strategy", "priority");
		
		LocatorUtil.setLocator(map, searchBut);
		
		return searchBut;
	}

	/**
	 * @param absEle
	 */
	private Button buildOperationBut(AbstractElement absEle)
	{
		Map<String, String> map = Collections.singletonMap(
				absEle.getDataStr(OPER_BUT_BY), absEle.getDataStr(OPER_BUT_INFO));
		map.put("strategy", "priority");
		
		LocatorUtil.setLocator(map, operationBut);
		
		return operationBut;
	}

	@Override
	public String getName()
	{
		return "jqgrid";
	}

	@Override
	public String getDescription()
	{
		return "基于jqgrid的连续操作";
	}

}
