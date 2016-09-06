/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.report;

import org.suren.autotest.web.framework.report.record.ExceptionRecord;

/**
 * 记录报告写入
 * @author suren
 * @date 2016年9月6日 下午8:45:39
 */
public interface RecordReportWriter
{
	/**
	 * 写入记录
	 * @param record
	 */
	void write(ExceptionRecord record);
}
