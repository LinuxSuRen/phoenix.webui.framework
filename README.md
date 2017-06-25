[![Build Status](https://travis-ci.org/LinuxSuRen/phoenix.webui.framework.svg?branch=master)](https://travis-ci.org/LinuxSuRen/phoenix.webui.framework)
[![License](https://img.shields.io/github/license/LinuxSuRen/phoenix.webui.framework.svg)](https://github.com/LinuxSuRen/phoenix.webui.framework/master/LICENSE)

# AutoTest
本项目是一个基于Selenium的Web自动测试框架，通过该框架可以简化测试人员的学习难度，只要编写少量的Java代码即可，大多数的工作都是编写页面元素的描述文件以及对应的数据源。以下是本框架的特色：
- 支持多种元素选择策略（优先级、循环、区域）
- 支持多种数据源（xml、excel）
- 支持数据源、URL、元素定位信息的参数化
- 支持密文数据，javascript、groovy、freemarker等动态脚本数据
- 支持动态生成日期、身份证号码、手机号、邮编等数据
- 支持操作日志生成GIF动态图片
- 支持主流的浏览器（ie、firefox、chrome、opera、safari）
- 支持移动自动化（Android）
- 支持Eclipse插件生成代码
- 支持纯XML编写完成自动化测试功能
- 自带Windows版本的driver驱动

# 测试报告
![Excel格式的测试报告](http://surenpi.com/wp-content/uploads/2017/06/autotest_report_excel_1.png)
![Excel格式的测试报告](http://surenpi.com/wp-content/uploads/2017/06/autotest_report_excel_2.png)
![数据库格式的测试报告](http://surenpi.com/wp-content/uploads/2017/06/report_database.png)

Maven的依赖，[更多示例](https://github.com/LinuxSuRen/phoenix.autotest.demo)。

# 远程的XSD地址
- http://surenpi.com/schema/autotest/autotest.web.framework.xsd
- http://surenpi.com/schema/datasource/autotest.web.framework.datasource.xsd
- http://surenpi.com/schema/suite/autotest.web.framework.suite.xsd

如果不知道如何配置Eclipse的XML智能提示，请访问下面的教程：  
http://surenpi.com/2016/07/21/eclipse_prompt_xml/  

本项目在码云和Github上都有托管，下面是各自的地址：  
Github  https://github.com/LinuxSuRen/phoenix.framework  
码云    http://git.oschina.net/arch2surenpi/phoenix  

# 浏览器兼容性
chrome v53-55  
ie8  
firefox

# 环境
JDK1.8，Maven  

更多详细内容请访问下面的博客：  
http://surenpi.com/2016/07/18/autotest_web_framework_base_selenium/

# 备注
由于本项目没有提交任何工程、IDE相关的文件（这样，您就可以任选Eclipse、IntelliJ IDEA或者是NetBeans作为您的开发工具了），所以check出来以后还需要一些步骤。  
这里给出在Eclipse中使用Maven的教程。  

QQ交流群：52492046  
加群后请及时修改备注为：城市-昵称
