/**
 * http://surenpi.com
 */
package org.suren.autotest.web.framework.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.report.RecordReportWriter;
import org.suren.autotest.web.framework.report.record.ExceptionRecord;
import org.suren.autotest.web.framework.report.record.SearchRecord;

/**
 * 有关元素查找所需时间的日志输出
 * @author suren
 * @date Jul 30, 2016 10:09:16 PM
 */
@Component
@Aspect
public class InfoLog
{
	private static final Logger LOGGER = LoggerFactory.getLogger(InfoLog.class);
	
	@Autowired
	private RecordReportWriter recordReportWriter;
	
	@Around("execution(* org.suren.autotest.web.framework.core.action.ClickAble.click*(..))")
	public Object clickAround(ProceedingJoinPoint joinPoint) throws Throwable
	{
		Object[] args = joinPoint.getArgs();
		
		return joinPoint.proceed(args);
	}
	
	@Around("execution(* org.suren.autotest.web.framework.core.ElementSearchStrategy.search*(..))")
	public Object searchLog(ProceedingJoinPoint joinPoint) throws Throwable
	{
		long beginTime = System.currentTimeMillis();
		
		Object[] args = joinPoint.getArgs();
		
		Object res = joinPoint.proceed(args);
		
		long endTime = System.currentTimeMillis();
		long cost = endTime - beginTime;
		
		ExceptionRecord exceptionRecord = new SearchRecord(cost);
		exceptionRecord.setMessage(joinPoint.getSignature().toLongString());
		recordReportWriter.write(exceptionRecord);
		
		return res;
	}

	/**
	 * @return the recordReportWriter
	 */
	public RecordReportWriter getRecordReportWriter()
	{
		return recordReportWriter;
	}

	/**
	 * @param recordReportWriter the recordReportWriter to set
	 */
	public void setRecordReportWriter(RecordReportWriter recordReportWriter)
	{
		this.recordReportWriter = recordReportWriter;
	}
}
