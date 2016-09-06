/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.report.RecordReportWriter;
import org.suren.autotest.web.framework.report.record.ExceptionRecord;

/**
 * 采用日志的形式记录报告
 * @author suren
 * @date 2016年9月6日 下午9:10:47
 */
@Component
public class LoggerRecordReport implements RecordReportWriter
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerRecordReport.class);
	
	@Override
	public void write(ExceptionRecord record)
	{
		LOGGER.debug(record.getMessage());
	}

}
