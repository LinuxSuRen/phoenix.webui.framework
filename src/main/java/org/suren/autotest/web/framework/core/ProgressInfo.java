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

package org.suren.autotest.web.framework.core;

/**
 * 传递进度信息的接口
 * @author suren
 * @date 2017年2月24日 下午4:27:15
 */
public interface ProgressInfo<T>
{
	/**
	 * 设置信息
	 * @param data
	 */
	void setInfo(T data);
	
	/**
	 * 设置唯一表示符
	 * @param id
	 */
	void setIdentify(String id);
	
	/**
	 * @return 返回唯一标识符
	 */
	String getIdentify();
	
	/**
	 * 设置状态
	 * @param status
	 */
	void setStatus(int status);
	
	/** 开始 */
	int ST_START = 0X1;
	/** 进行中 */
	int ST_PROGRESS = 0X2;
	/** 正常结束 */
	int ST_NORMAL_END = 0X3;
	/** 异常结束 */
	int ST_ERROR_END = 0X4;
}
