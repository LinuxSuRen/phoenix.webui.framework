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
