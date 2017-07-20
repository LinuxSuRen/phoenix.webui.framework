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

package org.suren.autotest.web.framework.settings;

/**
 * 支持的浏览器引擎常量
 * 
 * @author suren
 * @since Jul 17, 2016 9:33:30 AM
 */
public interface DriverConstants
{
	String	DRIVER_CHROME			= "chrome";
	String	DRIVER_IE				= "ie";
	String	DRIVER_FIREFOX			= "firefox";
	String	DRIVER_SAFARI			= "safari";
	String	DRIVER_OPERA			= "opera";
	String	DRIVER_PHANTOM_JS		= "phantomJs";
	String	DRIVER_HTML_UNIT		= "htmlUnit";

	/** 引擎配置文件名称 */
	String	ENGINE_CONFIG_FILE_NAME	= "engine.properties";
	
	/** 浏览器初始化启动地址 */
	String INITIAL_URL = "initialUrl";
}
