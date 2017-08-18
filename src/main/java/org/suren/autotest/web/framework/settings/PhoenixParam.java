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

import com.beust.jcommander.Parameter;

/**
 * 启动参数
 * @author <a href="http://surenpi.com">suren</a>
 * @since 2.1.0
 */
public class PhoenixParam
{
    @Parameter(names = "-browser", description = "指定要运行的浏览器类型，包括："
            + DriverConstants.DRIVER_CHROME + ","
            + DriverConstants.DRIVER_FIREFOX + ","
            + DriverConstants.DRIVER_IE + ","
            + DriverConstants.DRIVER_HTML_UNIT + ","
            + DriverConstants.DRIVER_OPERA + ","
            + DriverConstants.DRIVER_PHANTOM_JS,
            validateWith = {BrowserParamValidator.class})
    public String browser;
    
    @Parameter(names = "-browser-max", description = "最大化浏览器")
    public boolean browserMax;
    
    @Parameter(names = "-browser-version", description = "浏览器版本")
    public String browserVersion;
    
    @Parameter(names = "-remote", description = "远程服务器地址，例如：http://192.168.1.1:1234/wb/hub")
    public String remote;

    @Parameter(names = "-report-store", description = "测试报告输出目录，只适用于有文件输出的报告类型",
            validateWith = {FolderValidator.class})
    public String reportStore;

    @Parameter(names = "-printUsage", description = "打印帮助信息并退出程序")
    public boolean printUsage;
}
