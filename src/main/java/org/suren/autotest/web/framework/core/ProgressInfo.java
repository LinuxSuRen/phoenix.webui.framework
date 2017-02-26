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
 * @author suren
 * @date 2017年2月24日 下午4:27:15
 */
public interface ProgressInfo<T>
{
	void setInfo(T data);
	
	void setIdentify(String id);
	String getIdentify();
	
	void setStatus(int status);
	
	int ST_START = 0X1;
	int ST_PROGRESS = 0X2;
	int ST_NORMAL_END = 0X3;
	int ST_ERROR_END = 0X4;
}
