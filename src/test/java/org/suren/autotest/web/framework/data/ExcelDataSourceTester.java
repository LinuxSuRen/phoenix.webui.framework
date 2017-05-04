package org.suren.autotest.web.framework.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import org.suren.autotest.web.framework.data.ExcelDataSource;
import org.suren.autotest.web.framework.data.FileResource;

/**
 * excel测试类
 * @author suren
 * @date 2016年9月6日 下午9:27:07
 */
public class ExcelDataSourceTester {

//	@Test
//	public void excelDataSource() {
//		ExcelDataSource excelDataSource = new ExcelDataSource();
//		
//		excelDataSource.loadData(new FileResource(new File("D:/b.xlsx")), null);
//		
//		
//	}
	@Test
	public void excelDataSource() throws Exception {
		ExcelData excelDataSource = new ExcelData();
		
		excelDataSource.getWorkbook("D:/2017-05-01桥梁数据汇总情况表.xlsx");
		excelDataSource.readExcel("D:/2017-05-01桥梁数据汇总情况表.xlsx", 0);
		
		excelDataSource.readExcel("D:/2017-05-01桥梁数据汇总情况表.xlsx");
		excelDataSource.showExcel();
	}
	
}
