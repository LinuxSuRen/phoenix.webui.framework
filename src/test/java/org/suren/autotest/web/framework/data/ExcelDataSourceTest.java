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

package org.suren.autotest.web.framework.data;

import org.junit.Assert;
import org.junit.Test;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.DemoPage;

/**
 * excel测试类
 * @author suren
 * @date 2016年9月6日 下午9:27:07
 */
public class ExcelDataSourceTest
{

	@Test
	public void excelDataSource()
	{
		ExcelDataSource excelDataSource = new ExcelDataSource();

		DemoPage page = new DemoPage();
		page.setUserName(new Text());
		page.setPassword(new Text());
		
		boolean loadResult = excelDataSource.loadData(
				new ClasspathResource(ExcelDataSourceTest.class, "dataSource/excel/demo.xlsx"),
				page);
		
		Assert.assertTrue(loadResult);
		
		Assert.assertNotNull(page.getUserName().getValue());
		Assert.assertNotNull(page.getPassword().getValue());
	}
}
