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

package org.suren.autotest.web.framework.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.report.RecordReportWriter;
import org.suren.autotest.web.framework.report.record.ExceptionRecord;
import org.suren.autotest.web.framework.report.record.NormalRecord;
import org.suren.autotest.web.framework.report.record.ProjectRecord;

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
		LOGGER.error(record.getThrowable().getMessage());
	}

	@Override
	public void write(NormalRecord normalRecord)
	{
		LOGGER.info(normalRecord.toString());
	}

	@Override
	public void write(ProjectRecord projectRecord)
	{
	}

}
