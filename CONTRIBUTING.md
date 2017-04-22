# 介绍
如果您看好本项目，希望能参与到项目中来的话，将会是非常受欢迎的。  
为了能够更好地让大家参与到开源项目PhoenixFramework中来，下面介绍几种方式：
* 贡献issues，您可以在这里提出您的意见或者建议，还有bug
* 贡献文档，一个优秀的项目不仅仅是代码质量要好，文档也是必不可少的
* 贡献代码，如果您在使用的过程中发现有缺陷，也可以直接把修正后的代码贡献出来
* 贡献博文，为了能让更多的人了解到本项目，因此受益，您可以在您的博客中介绍本项目
# 贡献issues
在提出问题的时候，尽可能把遇到问题的环境因素（操作系统、Java版本、浏览器版本、数据库等）都写清楚，这样才能更加容易复现问题、修正问题。  
如果您愿意帮忙测试PhoenixFramework的兼容性的话，希望能从以下环境中进行测试：
* 操作系统，Windows（7，8,10）、Linux（Ubuntu、CentOS、Readhat）、Mac
* 浏览器，Firefox、Chrome、IE
# 贡献文档
我们在GitHub上的文档是用Markdown的格式来编写的，所以，如果您要给PhoenixFramework贡献文档的话，首先要了解Markdown的语法。
# 贡献代码
## Java
项目使用Maven来构建，所以，您需要对Maven有了解。对于框架部分，您需要对Spring、Selenium要有了解。  
编码规范：  
所有public修饰的方法必须要有注释，大括号采用上下对齐的方式  
属性注释格式  
```java
/** 状态 */
public static final int FETCH_SUCCESS = 1;
```
类注释格式（必须要有author标签）   
```java
/**
 * 用于回调的接口
 * @author suren
 * @date 2017年1月25日 上午11:38:41
 */
public interface Callback<T>
{
	void callback(T data);
}
```
赋值符号两边必须各有一个空格，例如：  
```java
String value = locatorMap.get(key);
```
## JavaScript
JS的框架使用的是Bootstrap。  
需要遵守的代码规范：  
如果一个页面中需要有多份onready这种页面加载完需要执行的逻辑的话，要写在同一地方。  
功能相似的函数要写在一起，不能过于分散。函数名为驼峰规则。
## Css
# 贡献博文
欢迎您在CSDN、博客园等站点上分享PhoenixFramework框架，介绍如何使用、高级特性等。