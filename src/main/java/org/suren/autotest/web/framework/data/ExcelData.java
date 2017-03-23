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

package org.suren.autotest.web.framework.data;

import org.suren.autotest.web.framework.core.ui.Button;

/**
 * 
 * 此处填写类简介
 * <p>
 * 此处填写类说明
 * </p>
 * @author sunyp
 * @since jdk1.6
 * 2016年6月24日
 *  
 */

public class ExcelData {

	/**
	 * TODO
	 * @param fieldName
	 * @return
	 */
	public Object getValue(String fieldName) {
		if("loginNameText".endsWith(fieldName)) {
			return "testUser";
		}else if("loginButton".equals(fieldName)){
			return new Button();
		}else{
			return "error";
		}
	}

}
