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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.suren.autotest.web.framework.core.ui.Button;

/**
 * 
 * 此处填写类简介
 * <p> EXCELData 工具类
 * 此处填写类说明
 * </p> 读取EXCEL 第一个sheet当做一个对象实体POJO ；其中规范：Sheetname=类名称； 第一行 每一列作为对象参数名称； 第二行开始作为每个对象的数据；
 * @author TEstdev
 * @since jdk1.6
 * 2017年5月7日
 *  
 */

public class ExcelData {
	/**
	 * @Workbook EXCEL工作簿对象
	 */
	public static Workbook Workbook;

	/**
	 * TODO
	 * @param fieldName 
	 * @return
	 */
	public Object getValue(String fieldName) 
	{
		if("loginNameText".endsWith(fieldName)) 
		{
			return "testUser";
		}
		else if("loginButton".equals(fieldName))
		{
			return new Button();
		}
		else
		{
			return "error";
		}
	}


	
	
	public void excel(String loaclpath) throws FileNotFoundException,IOException 
	{
		Workbook = getWorkbook(loaclpath);

	}

	/**
	 * 
	 * 
	 * @param path
	 *            文件路径
	 * @return 得到Excel工作簿对象
	 * @throws FileNotFoundException
	 * @throws IOException
	 */

	public static Workbook getWorkbook(String path)	throws FileNotFoundException, IOException 
	{
		Workbook book = null;
		try 
		{
			book = new XSSFWorkbook(new FileInputStream(path));

		} catch (Exception ex) 
		{
			book = new HSSFWorkbook(new FileInputStream(path));
		}

		return book;
	}


	/**
	 * 
	 * 
	 * 
	 * @param sheeetsize 获取指定的sheet对象0 代表第一个 1代表第二个
	 * @return
	 */
	public static Sheet getHSSFSheet(int sheeetsize) 
	{
		Sheet sheet = Workbook.getSheetAt(sheeetsize);
		return sheet;
	}

/**
 * 
 * @return 获取所有sheet集合
 */
	public static Sheet[] getHSSFSheets() 
	{
		Sheet[] sheets = new Sheet[50];
		Sheet st= null;
		if(Workbook!=null)
		{
			for (int i = 0; i < Workbook.getNumberOfSheets(); i++) 
			{// 获取每个Sheet表
				st = Workbook.getSheetAt(i);
				sheets[i] = st;
			}	
			
		}
		else
		{
			
		throw new RuntimeException("Excel工作簿对象为空请使用excel(String loaclpath)方法指定路径后调用该方法");
				
		}
		return sheets;
	}

	/**
	 * 
	 * @param sheet
	 *            获取行数
	 * @return
	 */
	public static int getRownumber(Sheet sheet) 
	{

		int rowcount = sheet.getLastRowNum();

		return rowcount;
	}

	/**
	 * 
	 * @param sheet
	 *            获取第一行所有列的数量
	 * @return
	 */
	public int getcolunNumnumber(Sheet sheet) 
	{
		int columnNum = sheet.getRow(0).getPhysicalNumberOfCells();
		return columnNum;
	}

	/**
	 * 
	 * 
	 * @param sheet获取sheet名称作为javabean名称
	 * @return
	 */
	public String getSheetName(Sheet sheet) 
	{
		String sheetname = sheet.getSheetName();
		return sheetname;
	}

	/**
	 * 
	 * 
	 * @param sheet
	 * @return 获取第一行参数作为bean各个参数属性
	 */
	public ArrayList<String> getcolums(Sheet sheet) 
	{
		ArrayList<String> getcolumslist = new ArrayList<String>();

		int a = getcolunNumnumber(sheet);
		for (int i = 0; i < a; i++) 
		{
			Cell cell = sheet.getRow(0).getCell(i);

			String vaule = cell.getStringCellValue();
			getcolumslist.add(vaule);
		}
		return getcolumslist;
	}

	/**
	 * 
	 * 
	 * @param sheet
	 * @return 获取第指定行参数作为bean数据
	 */

	public ArrayList<String> getcolums(Sheet sheet, int row) 
	{
		ArrayList<String> getcolumslist = new ArrayList<String>();
		int a = getcolunNumnumber(sheet);
		for (int i = 0; i < a; i++) {
			Cell cell = sheet.getRow(row).getCell(i);

			String vaule = cell.getStringCellValue();
			getcolumslist.add(vaule);
		}

		return getcolumslist;
	}


	/**
	 * 
	 * 
	 * @param fileName excel文件路径
	 * @return 获取所有sheet 的sheet名称为KEY, VALUE是String[] list  其中list角标为行数；
	 */
	public static Map<String, List<String[]>> readExcel(String fileName) 
	{
		Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
		
		try {
			Workbook = getWorkbook(fileName);
			// 循环工作表Sheet
			for (int numSheet = 0; numSheet < Workbook.getNumberOfSheets(); numSheet++) 
			{

				Sheet xssfSheet = Workbook.getSheetAt(numSheet);
				if (xssfSheet == null) 
				{
					continue;
				}
				String sheetname = xssfSheet.getSheetName();
				
				List<String[]> list = new ArrayList<String[]>();

				for (int row = 0; row <= xssfSheet.getLastRowNum(); row++) 
				{
					Row xssfRow = xssfSheet.getRow(row);
					if (xssfRow == null) 
					{
						continue;
					}
					
					String[] singleRow = new String[xssfRow.getLastCellNum()];
					
				for (int column = 0; column < xssfRow.getLastCellNum(); column++) 
				{
						Cell cell = xssfRow.getCell(column,
								Row.CREATE_NULL_AS_BLANK);
						switch (cell.getCellType()) 
						{
						case Cell.CELL_TYPE_BLANK:
							singleRow[column] = "";
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							singleRow[column] = Boolean.toString(cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_ERROR:
							singleRow[column] = "";
							break;
						case Cell.CELL_TYPE_FORMULA:
							cell.setCellType(Cell.CELL_TYPE_STRING);
							singleRow[column] = cell.getStringCellValue();
							if (singleRow[column] != null) 
							{
								singleRow[column] = singleRow[column].replaceAll("#N/A", "").trim();
							}
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) 
							{
								singleRow[column] = String.valueOf(cell.getDateCellValue());
							} 
							else 
							{
								cell.setCellType(Cell.CELL_TYPE_STRING);
								String temp = cell.getStringCellValue();
								// 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
								
								if (temp.indexOf(".") > -1) 
								{
									singleRow[column] = String.valueOf(new Double(temp)).trim();
								} 
								else 
								{
									singleRow[column] = temp.trim();
								}
								
							}

							break;
						case Cell.CELL_TYPE_STRING:
							singleRow[column] = cell.getStringCellValue()
									.trim();
							break;
						default:
							singleRow[column] = "";
							break;
						}
					}
					list.add(singleRow);
				}
				map.put(sheetname, list);
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return map;
	}
/**
 * 
 * @param fileLocation 本地文件路径
 * @param sheetnumber sheet的角标
 * @return MAP 安装行数 KEY是行数 value是数据集合；  获取指定的Sheet 返回所有数据对象
 * @throws IOException
 */
	public Map<Integer, String[]> readExcel(String fileLocation, int sheetnumber)throws IOException 
	{

		Map<Integer, String[]> data = new HashMap<>();

		Workbook = getWorkbook(fileLocation);
		int sheetsize = Workbook.getNumberOfSheets();
		if (sheetnumber > sheetsize - 1) 
		{
			return null;
		}
		Sheet sheet = Workbook.getSheetAt(sheetnumber);
		for (int row = 0; row <= sheet.getLastRowNum(); row++) 
		{
			Row xssfRow = sheet.getRow(row);
			if (xssfRow == null) 
			{
				continue;
			}
			String[] singleRow = new String[xssfRow.getLastCellNum()];
			for (int column = 0; column < xssfRow.getLastCellNum(); column++) 
			{
				Cell cell = xssfRow.getCell(column, Row.CREATE_NULL_AS_BLANK);
				switch (cell.getCellType()) 
				{
				case Cell.CELL_TYPE_BLANK:
					singleRow[column] = "";
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					singleRow[column] = Boolean.toString(cell
							.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_ERROR:
					singleRow[column] = "";
					break;
				case Cell.CELL_TYPE_FORMULA:
					cell.setCellType(Cell.CELL_TYPE_STRING);
					singleRow[column] = cell.getStringCellValue();
					if (singleRow[column] != null) 
					{
						singleRow[column] = singleRow[column].replaceAll(
								"#N/A", "").trim();
					}
					break;
					
				case Cell.CELL_TYPE_NUMERIC:
					
					if (DateUtil.isCellDateFormatted(cell)) 
					{
						singleRow[column] = String.valueOf(cell
								.getDateCellValue());
					} 
					else
					{
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String temp = cell.getStringCellValue();
						// 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串

						if (temp.indexOf(".") > -1) 
						{
							singleRow[column] = String
									.valueOf(new Double(temp)).trim();
						} 
						else
						{
							singleRow[column] = temp.trim();
						}
					}
					break;
				case Cell.CELL_TYPE_STRING:
					singleRow[column] = cell.getStringCellValue().trim();
					break;
					
				default:
					singleRow[column] = "";
					break;
				}
				
			}
			data.put(row, singleRow);
		}
		return data;
	}
	
}
