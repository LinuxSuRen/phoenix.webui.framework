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

package org.suren.autotest.web.framework.core.action;

import java.io.File;
import java.net.URL;

import org.suren.autotest.web.framework.core.ui.Element;

/**
 * 文件上传接口
 * @author suren
 * @since jdk1.7
 * 2016年7月19日
 */
public interface FileUploadAble extends Status
{
	/**
	 * 根据给定的URL来上传文件
	 * @param element
	 * @param url
	 * @return
	 */
	boolean upload(Element element, URL url);
	
	/**
	 * 根据给定的File对象来上传文件
	 * @param element
	 * @param file
	 * @return
	 */
	boolean upload(Element element, File file);
	
	/**
	 * 触发元素点击操作
	 * @param element
	 * @return
	 */
	boolean click(Element element);
}
