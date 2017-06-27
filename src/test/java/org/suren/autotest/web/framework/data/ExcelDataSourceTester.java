package org.suren.autotest.web.framework.data;

import java.io.File;

import org.junit.Test;

/**
 * excel测试类
 * @author suren
 * @date 2016年9月6日 下午9:27:07
 */
public class ExcelDataSourceTester {

	@Test
	public void excelDataSource() {
		ExcelDataSource excelDataSource = new ExcelDataSource();
		
		excelDataSource.loadData(new FileResource(new File("D:/b.xlsx")), null);
	}
}
