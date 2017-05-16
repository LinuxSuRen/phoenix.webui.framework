# 介绍
本项目PhoenixAutotest是一个基于Selenium的WebUI自动测试框架，通过该框架可以简化测试人员的学习难度，只要编写少量的Java代码即可，大多数的工作都是编写页面元素的描述文件以及对应的数据源。  
框架PhoenixAutotest已经集成了三大浏览器（Chrome、IE、Firefox）在Windows、Linux以及Mac操作系统下的各种WebDriver驱动版本。用户无需花费很多时间来进行开发环境的搭建。  
每一个做过WebUI自动化测试工作的朋友一定也体会到了，元素定位是一个比较耗时、困难的过程，针对这个痛点，框架也给出了三种元素定位的策略。  
而数据源、参数化更是本框架的一大亮点。您除了可以直接使用框架提供的丰富的数据源和参数化技术外，也可以很轻松地给出自己的实现。  
测试套件，也是无需用户编码，通过配置文件描述即可。这点，类似于TestNG的配置文件。

# 准备
如果要使用PhoenixAutotest框架来做WebUI自动化测试的话，需要先了解一些必要的技能或者知识。  
* XML
框架的元素定位信息、数据源等文件默认是XML格式的，所以，您应该了解XML的语法。
* Java基础
虽然，大多数的工作都交给XML配置了，但是还是应该了解Java的基础，例如：语法、集合、异常信息、调试技巧等。
* 开发工具
Eclipse或者其他IDE都是可以的，但您至少应该了解一种。
* 元素定位方法
WebUI测试，必然是离不开元素定位的。能够熟练地使用浏览器调试工具来获取元素定位信息是做自动化测试的基础。  

如果您对自动化测试感兴趣，但是现在还不了解Selenium的话，这里给您提供一篇[学习建议](http://surenpi.com/2016/04/28/selenium_learning_advise/)。
# 入门
步骤如下：
1. 创建工程
2. 配置Spring配置文件
3. 编写元素定位XML配置
4. 编写Page类
5. 编写测试类

## 创建工程
可以根据您的习惯来选择Java工程或者Maven工程。如果是Maven工程的话，请添加如下依赖：  
````xml
<dependency>
	<groupId>com.surenpi.autotest</groupId>
	<artifactId>autotest.web.framework</artifactId>
	<version>1.0.2-20170422</version>
</dependency>
````
如果只是要用简单的Java工程的话，可以从下面的QQ群里下载编译好的autotest.web.framework.dist-*.jar文件，加入到classpath中即可。

## Spring配置
````xml
<?xml version="1.0" encoding="UTF-8"?>
<p:beans xmlns:p="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context-3.0.xsd">
	<!-- 这里配置Page类所在的包，不然框架讲无法找到对应的类 -->
	<context:component-scan base-package="org.suren.autotest.test.page" />
</p:beans>
````
以上的配置文件applicationContext.xml，要放置在src根目录中。注意：该文件名固定，不允许自定义。

## 元素定位
```xml
<?xml version="1.0" encoding="UTF-8"?>
<suren:autotest xmlns:suren="http://surenpi.com"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://surenpi.com
		http://surenpi.com/schema/autotest/autotest.web.framework.xsd ">
	<suren:engine />
	<suren:pages>
		<suren:page class="org.suren.autotest.test.page.BaiduHomePage"
				url="https://www.baidu.com/">
			<suren:field name="keyword" byId="kw" />
			<suren:field name="searchBut" byId="su" />
		</suren:page>
	</suren:pages>
</suren:autotest>
```
以上的配置文件baidu.xml，要放置在src根目录中。
## Page类
```java
package org.suren.autotest.test.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.suren.autotest.web.framework.core.ui.Button;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;

/**
 * 逻辑页面，包含页面中需要定位的元素信息</br>
 * Text是文本框、文本域</br>
 * Button是按钮，可以是a标签或者input标签类型为button的以及其他所有可以电机的元素</br>
 * Selector是下拉列表</br>
 * 类上必须加Component，属性上必须加Autowired注解</br>
 * 所有的属性必须添加对应的getter和setter方法</br>
 * 另外，框架提供了工具类用于根据baidu.xml来生成当前Page类的方法，具体请查看code工程
 * @author suren
 * @date 2016年12月13日 下午7:57:44
 */
@Component
public class BaiduHomePage extends Page {
	@Autowired
	private Text keyword;
	@Autowired
	private Button searchBut;
	public Text getKeyword() {
		return keyword;
	}
	public void setKeyword(Text keyword) {
		this.keyword = keyword;
	}
	public Button getSearchBut() {
		return searchBut;
	}
	public void setSearchBut(Button searchBut) {
		this.searchBut = searchBut;
	}
}
````
## 测试类
```java
package org.suren.autotest.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.suren.autotest.test.page.BaiduHomePage;
import org.suren.autotest.web.framework.settings.SettingUtil;
import org.xml.sax.SAXException;

/**
 * AutoTest框架的一个简单示例</br>
 * baidu.xml主要包含元素定位信息的描述</br>
 * applicationContext.xml配置了Page类所在的包（package）</br>
 * Page类BaiduHomePage是一个逻辑的页面对象，包括页面中需要定位的元素
 * @author suren
 * @date 2016年12月13日 下午7:52:06
 */
public class Test {

	/**
	 * 入口函数
	 * @param args
	 * @throws IOException
	 * @throws DocumentException
	 * @throws SAXException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, DocumentException,
		SAXException, InterruptedException {
		SettingUtil util = new SettingUtil();
		util.readFromClassPath("baidu.xml"); //加载元素定位配置文件
		
		//获取Page类，然后获取对应的元素，再进行操作
		BaiduHomePage baiduHomePage = util.getPage(BaiduHomePage.class);
		baiduHomePage.open(); //打开baidu.xml配置文件中配置的页面地址
		baiduHomePage.getKeyword().setValue("selenium"); //告诉框架文本框要填充的值
		baiduHomePage.getKeyword().fillValue(); //向文本框中填充值
		baiduHomePage.getSearchBut().click(); //点击搜索按钮
		
		Thread.sleep(3000);
		
		util.close(); //关闭框架
	}
}
```
以上示例的[源代码](https://github.com/LinuxSuRen/phoenix.autotest.demo/tree/master/simple)
# 参考
[更多示例](https://github.com/LinuxSuRen/phoenix.autotest.demo)