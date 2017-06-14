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

package org.suren.autotest.web.framework.report.record;

import java.util.Date;

/**
 * 异常信息记录
 * @author suren
 * @date 2016年9月6日 下午8:36:15
 */
public class ExceptionRecord extends Record
{
	private boolean exception;
	private String message;
	private Throwable throwable;
	private Date time;
	/**
	 * @return the exception
	 */
	public boolean isException()
	{
		return exception;
	}
	/**
	 * @param exception the exception to set
	 */
	public void setException(boolean exception)
	{
		this.exception = exception;
	}
	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	/**
	 * @return the time
	 */
	public Date getTime()
	{
		return time;
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(Date time)
	{
		this.time = time;
	}
	/**
	 * @return the throwable
	 */
	public Throwable getThrowable()
	{
		return throwable;
	}
	/**
	 * @param throwable the throwable to set
	 */
	public void setThrowable(Throwable throwable)
	{
		this.throwable = throwable;
	}
}
