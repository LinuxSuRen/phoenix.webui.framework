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

package org.suren.autotest.web.framework.code;

import java.io.InputStream;

import org.dom4j.DocumentException;
import org.suren.autotest.web.framework.core.Callback;
import org.xml.sax.SAXException;

/**
 * 生成器
 * @author suren
 * @date 2016年12月3日 下午8:41:01
 */
public interface Generator
{
	/**
	 * 根据源文件生成Java代码
	 * @param srcCoding 源文件路径
	 * @param outputDir 输出的目录
	 * @return
	 */
	void generate(String srcCoding, String outputDir);
	
	void generate(InputStream input, String outputDir, Callback<?> callback)
			throws DocumentException, SAXException;
}
