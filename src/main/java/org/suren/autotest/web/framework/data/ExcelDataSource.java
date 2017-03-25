/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.page.Page;

/**
 * Excel格式的数据源实现类
 * @author suren
 * @date Jul 17, 2016 8:56:31 AM
 */
@Component("excel_data_source")
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExcelDataSource implements DataSource, DynamicDataSource
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDataSource.class);
	
	/** 要填充的page对象 */
	private Page targetPage;
	
	private List<Page> pageCache = new LinkedList<Page>();
	private Set<DataResource> dataResourceSet = new HashSet<DataResource>();

	@Override
	public boolean loadData(DataResource resource, Page page)
	{
		this.targetPage = page;
		
		if(!dataResourceSet.contains(resource))
		{
			URL url = null;
			
			try {
				url = resource.getUrl();
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			
			if(url == null)
			{
				return false;
			}
			
			try(InputStream inputStream = url.openStream()) //打开文件流
			{
				parse(inputStream); //解析excel数据源文件
			}
			catch (IOException e)
			{
				LOGGER.error(e.getMessage(), e);
			}
		}
		
		return false;
	}

	/**
	 * 解析excel数据源文件
	 * @param inputStream
	 * @throws IOException 
	 */
	private void parse(InputStream inputStream) throws IOException
	{
		Workbook workbook = new XSSFWorkbook(inputStream);
		
		Sheet sheet = workbook.getSheetAt(0);
		
		sheetParse(sheet);
	}

	/**
	 * 解析sheet内容
	 * @param sheet
	 */
	private void sheetParse(Sheet sheet)
	{
		Row headerRow = sheet.getRow(0);
		
		Iterator<Cell> cellIterator = headerRow.iterator();
		while(cellIterator.hasNext())
		{
			Cell cell = cellIterator.next();
			System.out.println(cell.getStringCellValue());
		}
		
		rowParse(null);
	}
	
	/**
	 * 行解析
	 * @param row
	 */
	private void rowParse(Row row)
	{
	}

	@Override
	public boolean loadData(DataResource resource, int row, Page page)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setGlobalMap(Map<String, Object> globalMap)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getGlobalMap()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
