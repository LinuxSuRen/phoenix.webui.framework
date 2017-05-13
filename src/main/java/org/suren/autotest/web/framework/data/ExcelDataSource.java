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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.util.StringUtils;

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
	private int maxRows = 100;
	
	private Set<DataResource> dataResourceSet = new HashSet<DataResource>();

	@Override
	public boolean loadData(DataResource resource, Page page)
	{
		return loadData(resource, 0, page);
	}

	/**
	 * 解析excel数据源文件
	 * @param inputStream
	 * @throws IOException 
	 */
	private void parse(InputStream inputStream) throws IOException
	{
		String name = targetPage.getClass().getName();
		Workbook workbook = new XSSFWorkbook(inputStream);
		
		Sheet sheet = workbook.getSheet(name);
		if(sheet == null)
		{
			int index = name.lastIndexOf(".");
			if(index > 0)
			{
				name = name.substring(index + 1);
				sheet = workbook.getSheet(name);
			}
		}
		
		sheetParse(sheet);
	}

	/**
	 * 解析sheet内容
	 * @param sheet
	 */
	private void sheetParse(Sheet sheet)
	{
		if(sheet == null)
		{
			return;
		}

		for(int i = 1; i < maxRows; i++)
		{
			Row row = sheet.getRow(i);
			if(row == null)
			{
				break;
			}
			
			cellParse(row);
		}
	}

	/**
	 * 解析一行数据
	 * @param row
	 */
	private void cellParse(Row row)
	{
		Class<?> targetCls = targetPage.getClass();
		
		Cell nameCell = row.getCell(0);
		Cell dataCell = row.getCell(1);
		
		if(nameCell == null || dataCell == null)
		{
			return;
		}
		
		try
		{
			String fieldName = getStrFromCell(nameCell);
			if(StringUtils.isBlank(fieldName))
			{
				return;
			}
			
			Field field = targetCls.getDeclaredField(fieldName);
			field.setAccessible(true);
		
			setValue(field, targetPage, getStrFromCell(dataCell));
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 从单元格中获取字符串
	 * @param dataCell
	 * @return 支持字符串、布尔值、数字，其他情况返回null
	 */
	private String getStrFromCell(Cell dataCell)
	{
		int cellType = dataCell.getCellType();
		switch(cellType)
		{
			case Cell.CELL_TYPE_STRING:
				return dataCell.getStringCellValue();
			case Cell.CELL_TYPE_BOOLEAN:
				return String.valueOf(dataCell.getBooleanCellValue());
			case Cell.CELL_TYPE_NUMERIC:
				return String.valueOf(dataCell.getNumericCellValue());
			default:
				return null;
		}
	}

	/**
	 * @param field
	 * @param targetPage
	 * @param data
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 */
	private void setValue(Field field, Page targetPage, Object data) throws IllegalArgumentException,
		IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException
	{
		field.setAccessible(true);
		Object fieldObj = field.get(targetPage);
		if(fieldObj == null)
		{
			return;
		}
		
		Method method = fieldObj.getClass().getMethod("setValue", String.class);
		
		method.invoke(fieldObj, data.toString());
	}

	@Override
	public boolean loadData(DataResource resource, int row, Page page)
	{
		this.targetPage = page;
		
		if(!dataResourceSet.contains(resource))
		{
			URL url = null;
			
			try
			{
				url = resource.getUrl();
			}
			catch(IOException e)
			{
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
		
		return true;
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
