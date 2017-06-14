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

package org.suren.autotest.web.framework.report;

import org.suren.autotest.web.framework.report.record.ExceptionRecord;
import org.suren.autotest.web.framework.report.record.NormalRecord;

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

	/**
	 * @param normalRecord
     */
	void write(NormalRecord normalRecord);
}
