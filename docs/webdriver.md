# Download Driver manually

This framework can help you download the WebDriver automatically. But in some cases you might 
need to download it manually. For example, you want to run your test cases in a container with 
the headless mode. A good practice is that prepare the WebDriver when you build the container image.

You can do it via: `java org.suren.autotest.web.framework.selenium.SeleniumEngine -browser chrome -version 62`
