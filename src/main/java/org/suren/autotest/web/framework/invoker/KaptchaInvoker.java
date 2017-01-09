/**
 * http://surenpi.com
*/
package org.suren.autotest.web.framework.invoker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.suren.autotest.interfaces.framework.HttpApiUtil;
import org.suren.autotest.interfaces.framework.param.AtCookie;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;

/**
 * @author suren
 * @date 2017年1月7日 下午9:21:14
 */
public class KaptchaInvoker {
	/**
	 * 获取验证码
	 * @param engine
	 * @param param 例如：data,http://localhost:8080/G2/captcha!getLastCode.do
	 * @return
	 */
	public static String execute(SeleniumEngine engine, String param) {
		WebDriver driver = engine.getDriver();
		Options manage = driver.manage();
		
		String[] paramArray = param.split(",", 2);
		
		if(paramArray.length != 2)
		{
			throw new RuntimeException("Param format is error, should be 'data,url'");
		}
		
		String key = paramArray[0];
		String url = paramArray[1];
		
		Set<Cookie> cookies = manage.getCookies();
		List<AtCookie> atCookieList = new ArrayList<AtCookie>();
		for(Cookie cookie : cookies) {
			String name = cookie.getName();
			String value = cookie.getValue();
			
			AtCookie atCookie = new AtCookie();
			atCookie.setName(name);
			atCookie.setValue(value);
			atCookie.setPath(cookie.getPath());
			atCookie.setDomain(cookie.getDomain());
			
			atCookieList.add(atCookie);
		}
		
		String code = HttpApiUtil.getJsonValue(url, atCookieList, key);
		
		return code;
	}
}
