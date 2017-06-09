/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.core;

/**
 * 套件的进度信息实现
 * @author suren
 * @date 2017年2月24日 下午4:27:57
 */
public class SuiteProgressInfo implements ProgressInfo<String>
{
	private String msg;
	private int status;
	private String identify;

	@Override
	public void setInfo(String data)
	{
		this.msg = data;
	}

	@Override
	public void setIdentify(String id)
	{
		this.identify = id;
	}

	@Override
	public void setStatus(int status)
	{
		this.status = status;
	}

	/**
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * @return the status
	 */
	public int getStatus()
	{
		return status;
	}

	/**
	 * @return the identify
	 */
	public String getIdentify()
	{
		return identify;
	}
}
