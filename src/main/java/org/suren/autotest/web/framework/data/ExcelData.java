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
 * <p>
 * 此处填写类说明
 * </p>
 * @author sunyp
 * @since jdk1.6
 * 2016年6月24日
 *  
 */

public class ExcelData {

	/**
	 * TODO
	 * @param fieldName
	 * @return
	 */
	public Object getValue(String fieldName) {
		if("loginNameText".endsWith(fieldName)) {
			return "testUser";
		}else if("loginButton".equals(fieldName)){
			return new Button();
		}else{
			return "error";
		}
	}

public static Workbook Workbook;

	
	
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
	 * @param hw
	 *            获取sheet对象
	 * @param sheeetsize
	 * @return
	 */
	public static Sheet getHSSFSheet(int sheeetsize) 
	{
		Sheet sheet = Workbook.getSheetAt(sheeetsize);
		return sheet;
	}

	@SuppressWarnings("null")
	public static Sheet[] getHSSFSheets() 
	{
		Sheet[] sheets = null;
		Sheet st;
		for (int i = 0; i < Workbook.getNumberOfSheets(); i++) {// 获取每个Sheet表
			st = Workbook.getSheetAt(i);
			sheets[i] = st;
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
	 *            获取列的数量
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
	 * @param sheet获取sheet名称作为bean名称
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
	 * @return 获取第一行参数作为bean属性
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

	public ArrayList<String> getcolums(Sheet sheet, int row) {
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
 * 打印出数据
 * @throws Exception
 */
	public void showExcel() throws Exception {

		for (int i = 0; i < Workbook.getNumberOfSheets(); i++) {// 获取每个Sheet表
			Sheet sheet = Workbook.getSheetAt(i);
			for (int j = 0; j < sheet.getPhysicalNumberOfRows(); j++) {// 获取每行
				Row row = sheet.getRow(j);
				for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {// 获取每个单元格

					if (k == row.getPhysicalNumberOfCells() - 1) {

						System.out.print(row.getCell(k) + "\n");
					} else {
						System.out.print(row.getCell(k) + "\t");
					}

				}

			}
			System.out.println("---Sheet表" + sheet.getSheetName() + "处理完毕---");
		}
	}

	@SuppressWarnings("deprecation")
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
				String sheetname = xssfSheet.getSheetName();

				if (xssfSheet == null) 
				{
					continue;
				}
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
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return map;
	}
/**
 * 
 * @param fileLocation 本地文件路径
 * @param sheetnumber sheet的角标
 * @return MAP 安装行数 KEY是行数 value是数据集合；
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
				switch (cell.getCellType()) {
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
					} else {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String temp = cell.getStringCellValue();
						// 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串

						if (temp.indexOf(".") > -1) 
						{
							singleRow[column] = String
									.valueOf(new Double(temp)).trim();
						} else
						
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
