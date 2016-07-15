/**
* Copyright © 1998-2016, Glodon Inc. All Rights Reserved.
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
