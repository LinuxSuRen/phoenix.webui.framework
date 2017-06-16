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
import org.suren.autotest.web.framework.report.record.ProjectRecord;

/**
 * 记录报告写入
 * @author suren
 * @date 2016年9月6日 下午8:45:39
 */
public interface RecordReportWriter
{
	/**
	 * 写入异常结果信息
	 * @param record
	 */
	void write(ExceptionRecord record);

	/**
	 * 写入正常结果信息
	 * @param normalRecord
     */
	void write(NormalRecord normalRecord);

	/**
	 * 写入项目信息
	 * @param projectRecord
     */
	void write(ProjectRecord projectRecord);
}
