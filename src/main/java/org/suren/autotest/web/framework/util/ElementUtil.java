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

package org.suren.autotest.web.framework.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.surenpi.autotest.webui.Page;
import com.surenpi.autotest.webui.ui.Button;
import com.surenpi.autotest.webui.ui.Element;
import com.surenpi.autotest.webui.ui.Text;

/**
 * 元素操作工具类，可以批量地操作元素
 * @author suren
 * @since 2017年5月14日 下午9:10:29
 */
public class ElementUtil
{
	/**
	 * 批量点击按钮
	 * @param buttons buttons
	 */
	public static void click(Button ...buttons)
	{
		if(buttons == null)
		{
			return;
		}
		
		List<Button> buttonList = Arrays.asList(buttons);
		buttonList.stream().sorted(getComparator()).forEach(but -> {
			but.click();
		});
	}
	
	/**
	 * 批量填充文本
	 * @param texts texts
	 */
	public static void fillValue(Text ...texts)
	{
		if(texts == null)
		{
			return;
		}
		
		Arrays.asList(texts).stream().sorted(getComparator()).forEach(text -> {
			text.fillValue();
		});
	}
	
	/**
	 * 批量对页面中的按钮进行点击操作
	 * @param pages pages
	 */
	public static void click(Page ...pages)
	{
		if(pages == null)
		{
			return;
		}
		
		for(Page page : pages)
		{
			List<Button> buttonList = new ArrayList<Button>();
			
			Class<? extends Page> pageCls = page.getClass();
			for(Field field : pageCls.getDeclaredFields())
			{
				if(field.getType().equals(Button.class))
				{
					field.setAccessible(true);
					try
					{
						buttonList.add((Button) field.get(page));
					}
					catch (IllegalArgumentException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			if(buttonList.size() > 0)
			{
				click(buttonList.toArray(new Button[]{}));
			}
		}
	}
	
	/**
	 * 批量对页面中的文本进行填入值操作
	 * @param pages pages
	 */
	public static void fillValue(Page ...pages)
	{
		if(pages == null)
		{
			return;
		}
		
		for(Page page : pages)
		{
			List<Text> textList = new ArrayList<Text>();
			
			Class<? extends Page> pageCls = page.getClass();
			for(Field field : pageCls.getDeclaredFields())
			{
				if(field.getType().equals(Text.class))
				{
					field.setAccessible(true);
					try
					{
						textList.add((Text) field.get(page));
					}
					catch (IllegalArgumentException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
			}
			
			if(textList.size() > 0)
			{
				fillValue(textList.toArray(new Text[]{}));
			}
		}
	
	}
	
	/**
	 * 根据元素的序号进行排序
	 * @return 元素排序的方法
	 */
	public static Comparator<Element> getComparator()
	{
		return (e1, e2) -> {
			return e1.getIndex() - e2.getIndex();
		};
	}
}
