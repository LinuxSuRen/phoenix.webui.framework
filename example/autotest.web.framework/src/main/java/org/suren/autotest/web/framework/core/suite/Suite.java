/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.core.suite;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试套件对象
 * @author suren
 * @date 2016年9月7日 下午9:43:32
 */
public class Suite
{
	/** 当前测试套件所在路径 */
	private URL pathUrl;
	/** page类描述文件 */
	private String xmlConfPath;
	/** page类的包 */
	private String pagePackage;
	/** Page对象列表，按照该顺序来执行任务 */
	private List<SuitePage> pageList;
	/** 测试套件运行结束后的休眠时间（毫秒） */
	private long afterSleep;
	/** 当前测试流程使用的数据集合，例如：1...3,5,7，默认执行第一组数据 */
	private String rows;
	/** 当前缺少指定数据组时的具体行为，默认将会采用最近的数据组 */
	private String lackLines;
	/** 当使用当前数据组运行报错时的具体行为，默认将会停止程序 */
	private String errorLines;

	/**
	 * @return the xmlConfPath
	 */
	public String getXmlConfPath()
	{
		return xmlConfPath;
	}

	/**
	 * @param xmlConfPath the xmlConfPath to set
	 */
	public void setXmlConfPath(String xmlConfPath)
	{
		this.xmlConfPath = xmlConfPath;
	}

	/**
	 * @return the pageList
	 */
	public List<SuitePage> getPageList()
	{
		return pageList;
	}

	/**
	 * @param pageList the pageList to set
	 */
	public void setPageList(List<SuitePage> pageList)
	{
		this.pageList = pageList;
	}

	/**
	 * @return the afterSleep
	 */
	public long getAfterSleep()
	{
		return afterSleep;
	}

	/**
	 * @param afterSleep the afterSleep to set
	 */
	public void setAfterSleep(long afterSleep)
	{
		this.afterSleep = afterSleep;
	}

	/**
	 * @return the rows
	 */
	public String getRows()
	{
		return rows;
	}
	
	/**
	 * 解析测试数据组序号
	 * @return
	 */
	public Integer[] getRowsArray()
	{
		if(rows != null)
		{
			String[] rowStrArray = rows.split(",");
			List<Integer> rowList = new ArrayList<Integer>(rowStrArray.length);
			for(String rowStr : rowStrArray)
			{
				try
				{
					rowList.add(Integer.parseInt(rowStr));
				}catch(NumberFormatException e){
					omitIndexParse(rowStr, rowList);
				}
			}
			
			return rowList.toArray(new Integer[]{});
		}
		else
		{
			return null;
		}
	}

	/**
	 * 省略时的写法，例如：1...3
	 * @param rowStr
	 * @param rowList
	 */
	private void omitIndexParse(String rowStr, List<Integer> rowList)
	{
		if(!rowStr.contains("..."))
		{
			return;
		}
		
		String targetStr = rowStr.replace("...", ",");
		String[] targetStrArray = targetStr.split(",");
		if(targetStrArray.length != 2)
		{
			return;
		}
		
		try
		{
			int from = Integer.parseInt(targetStrArray[0]);
			int to = Integer.parseInt(targetStrArray[1]);
			for(int i = from; i <= to; i++)
			{
				rowList.add(i);
			}
		}catch(NumberFormatException e){}
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(String rows)
	{
		this.rows = rows;
	}

	/**
	 * @return the lackLines
	 */
	public String getLackLines()
	{
		return lackLines;
	}

	/**
	 * @param lackLines the lackLines to set
	 */
	public void setLackLines(String lackLines)
	{
		this.lackLines = lackLines;
	}

	/**
	 * @return the errorLines
	 */
	public String getErrorLines()
	{
		return errorLines;
	}

	/**
	 * @param errorLines the errorLines to set
	 */
	public void setErrorLines(String errorLines)
	{
		this.errorLines = errorLines;
	}

	/**
	 * @return the pagePackage
	 */
	public String getPagePackage()
	{
		return pagePackage;
	}

	/**
	 * @param pagePackage the pagePackage to set
	 */
	public void setPagePackage(String pagePackage)
	{
		this.pagePackage = pagePackage;
	}

	public URL getPathUrl()
	{
		return pathUrl;
	}

	public void setPathUrl(URL pathUrl)
	{
		this.pathUrl = pathUrl;
	}
}
