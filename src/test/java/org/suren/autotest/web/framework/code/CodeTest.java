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

import org.junit.Test;

/**
 * 代码生成单元测试
 * @author suren
 * @date 2016年12月3日 下午8:59:07
 */
public class CodeTest
{
	@Test
	public void testPage()
	{
		new DefaultXmlCodeGenerator().generate("elements/xml/maimai.xml", "target");
	}
}
