import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.suren.autotest.web.framework.data.DataResource;
import org.suren.autotest.web.framework.data.ExcelDataSource;

/**
 * Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
 */

/**
 * 
 * 此处填写类简介
 * <p>
 * 此处填写类说明
 * </p>
 * @author glodon
 * @since jdk1.6
 * 2016年9月5日
 *  
 */

public class ExcelDataSourceTester {

	public void test()
	{
	}
	
	@Test
	public void main() {
		ExcelDataSource ExcelDataSource = new ExcelDataSource();
		
		ExcelDataSource.loadData(new DataResource() {
			
			@Override
			public URL getUrl() {
				// TODO Auto-generated method stub
				try {
					return new File("D:/b.xlsx").toURL();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
		}, null);
	}
}
