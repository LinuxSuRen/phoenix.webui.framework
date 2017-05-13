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

package org.suren.autotest.web.framework.core;

/**
 * 操作鼠标的接口
 * @author suren
 * @date 2016年11月25日 上午8:05:50
 */
public interface Mouse
{
	/**
	 * 向下滚动一次
	 */
	void wheel();
	/**
	 * 向下滚动n次
	 * @param num
	 */
	void wheel(int num);
	/**
	 * 左单击
	 */
	void click();
	/**
	 * 右单击
	 */
	void rightClick();
}
