[![License](https://img.shields.io/github/license/LinuxSuRen/phoenix.webui.framework.svg)](https://github.com/LinuxSuRen/phoenix.webui.framework/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.surenpi.autotest/autotest.web.framework/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.surenpi.autotest/autotest.web.framework)

# PhoenixAutoTest

本项目是一个基于 [Selenium](https://github.com/seleniumhq/selenium/) 的Web自动测试框架，通过该框架可以简化测试人员的学习难度，只要
编写少量的Java代码即可，大多数的工作都是编写页面元素的描述文件以及对应的数据源。以下是本框架的特色：

- 支持多种元素选择策略（优先级、循环、区域）
- 支持多种[数据源](https://github.com/LinuxSuRen/autotest.datasource)（[yaml](https://github.com/LinuxSuRen/autotest.datasource.yaml)、[excel](https://github.com/LinuxSuRen/autotest.datasource.excel)）
- 支持包括：[HTML](https://github.com/LinuxSuRen/autotest.report.html)、[Excel](https://github.com/LinuxSuRen/autotest.report.excel)、[Jira](https://github.com/LinuxSuRen/autotest.report.jira) 、[数据库](https://github.com/LinuxSuRen/autotest.report.database)等格式的[测试报告](https://github.com/LinuxSuRen/autotest.report)输出
- 支持数据源、URL、元素定位信息的参数化
- 支持密文数据，javascript、groovy、freemarker等动态脚本数据
- 支持动态生成日期、身份证号码、手机号、邮编等数据
- 支持操作日志生成GIF动态图片
- 支持主流的浏览器（ie、firefox、chrome、opera、safari）以及 headless 模式
- 支持纯 XML 编写完成自动化测试功能
- [自动下载](https://github.com/linuxsuren/autotest.webdriver.downloader) WebDriver 驱动

# 快速开始

你可以先下载这个[示例项目](https://github.com/LinuxSuRen/phoenix.webui.framework.demo)，来快速地体验。

# 引擎配置

你可以通过引擎配置文件 `engine.properties` 调整框架的默认行为，支持的配置项包括：

| Key | 默认值 | 描述 |
|---|---|---|
| `engine.autoLoad` | `true` | 是否自动下载 WebDriver |
| `cookie.save` | `false` | |
| `cookie.save.path` | `phoenix.autotest.cookie` | |
| `chrome.version` | | Chrome 浏览器版本 |
| `chrome.args.headless` | `false` | |
| `chrome.args.intl.accept_languages` | | |
| `chrome.args.lang` | | 浏览器语言设置，例如：`zh_CN` |
| `chrome.args.window-size=` | | 设置浏览器窗口大小，例如：`1024,768` |

在 Maven 项目中，该文件的位置是：`src/main/resources/engine.properties`。你可以从[ Chrome 官方文档](https://sites.google.com/a/chromium.org/chromedriver/capabilities)
查找完整的参数。

# 元素定位

## 国际化页面

对于具有国际化的前端页面，浏览器的语言不同的情况下页面元素会有不同的展示。例如：一个按钮中文时显示为：`确认`，英文时显示为：`OK`。
此时，如果我们希望通过这里的文本字符串来定位的话，可以参考下面的示例，同时提供多个定位方法，并指定语言：

```
@AutoLocators(strategy = StrategyType.CYLE, locators = {
    @AutoLocator(locator = LocatorType.BY_XPATH, value = "//p[contains(text(),'企业空间')]", lang = "zh-CN"),
    @AutoLocator(locator = LocatorType.BY_XPATH, value = "//p[contains(text(),'Workspaces')]")
})
```
