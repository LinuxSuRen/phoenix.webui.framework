/*
 *
 *  * Copyright 2002-2007 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package org.suren.autotest.web.framework.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.suren.autotest.web.framework.annotation.AutoCookie;
import org.suren.autotest.web.framework.annotation.AutoExpect;
import org.suren.autotest.web.framework.annotation.AutoItem;
import org.suren.autotest.web.framework.annotation.AutoLocalStorage;
import org.suren.autotest.web.framework.annotation.AutoModule;
import org.suren.autotest.web.framework.annotation.AutoSessionStorage;
import org.suren.autotest.web.framework.log.LoggerConstants;

import com.surenpi.autotest.report.RecordReportWriter;
import com.surenpi.autotest.report.record.ExceptionRecord;
import com.surenpi.autotest.report.record.NormalRecord;
import com.surenpi.autotest.utils.PathUtil;
import com.surenpi.autotest.webui.Page;
import com.surenpi.autotest.webui.core.AutoTestException;
import com.surenpi.autotest.webui.ui.Text;

/**
 * 模块代理类。通过代理的方式来实现测试报告的输出。
 * @author <a href="http://surenpi.com">suren</a>
 */
public class AutoModuleProxy implements MethodInterceptor
{
    private static final Logger logger = LoggerFactory.getLogger(AutoModuleProxy.class);
    
    private Enhancer enhancer = new Enhancer();
    private Object target;
    private List<RecordReportWriter> recordReportWriters;
    private Phoenix util;

    public AutoModuleProxy(Object target, List<RecordReportWriter> recordReportWriters, Phoenix util)
    {
        this.target = target;
        this.recordReportWriters = recordReportWriters;
        this.util = util;
    }

	public Object getProxy()
    {
        Class<?> clazz = target.getClass();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        enhancer.setClassLoader(target.getClass().getClassLoader());
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy methodProxy) throws Throwable
    {
        long beginTime = System.currentTimeMillis();

        Object result = null;
        Class<?> superCls = obj.getClass().getSuperclass();
        AutoModule autoModule = superCls.getAnnotation(AutoModule.class);
        AutoExpect autoExpect = method.getAnnotation(AutoExpect.class);
        AutoSessionStorage autoSessionStorage = method.getAnnotation(AutoSessionStorage.class);
        AutoLocalStorage autoLocalStorage = method.getAnnotation(AutoLocalStorage.class);
        AutoCookie autoCookie = method.getAnnotation(AutoCookie.class);

        NormalRecord normalRecord = new NormalRecord();
        normalRecord.setBeginTime(beginTime);
        normalRecord.setClazzName(superCls.getName());
        normalRecord.setMethodName(method.getName());
        normalRecord.setModuleName(autoModule.name());
        normalRecord.setModuleDescription(autoModule.description());

        try
        {
            SessionStorageConfig sessionStorageConfig = new SessionStorageConfig();
            if(autoSessionStorage != null)
            {
                sessionStorageConfig.setAutoLoad(true);
                Class<? extends Page> accountClz = autoSessionStorage.pageClazz();
                String accountNameField = autoSessionStorage.sessionKey();

                Page page = util.getPage(accountClz);
                Field accountField = accountClz.getDeclaredField(accountNameField);

                accountField.setAccessible(true);
                Object value = accountField.get(page);

                if(value instanceof Text)
                {
                    String accountNameValue = ((Text) value).getValue();
                    sessionStorageConfig.setAccount(accountNameValue);
                    
                    Map<String, String> customMap = new HashMap<String, String>();
                    AutoItem[] overItems = autoSessionStorage.overItems();
                    if(overItems != null && overItems.length > 0)
                    {
                    	Arrays.asList(overItems).forEach((item) -> {
                    		customMap.put(item.key(), item.value());
                    	});
                    }

                    page.open();
                    if(loadSessionStorage(accountNameValue, customMap))
                    {
                        sessionStorageConfig.setAccount(accountNameValue);

                        if(autoSessionStorage.skipMethod())
                        {
                            sessionStorageConfig.setSkipLogin(true);
                        }
                    }
                }
                else
                {
                    throw new AutoTestException("Wrong account type in class: " + accountClz + ", and field : "
                            + accountNameField + ". It should be Text type.");
                }
            }
            
            LocalStorageConfig localStorageConfig = new LocalStorageConfig();
            if(autoLocalStorage != null)
            {
            	localStorageConfig.setAutoLoad(true);
                Class<? extends Page> accountClz = autoLocalStorage.pageClazz();
                String accountNameField = autoLocalStorage.sessionKey();

                Page page = util.getPage(accountClz);
                Field accountField = accountClz.getDeclaredField(accountNameField);

                accountField.setAccessible(true);
                Object value = accountField.get(page);

                if(value instanceof Text)
                {
                    String accountNameValue = ((Text) value).getValue();
                    localStorageConfig.setAccount(accountNameValue);
                    
                    Map<String, String> customMap = new HashMap<String, String>();
                    AutoItem[] overItems = autoLocalStorage.overItems();
                    if(overItems != null && overItems.length > 0)
                    {
                    	Arrays.asList(overItems).forEach((item) -> {
                    		customMap.put(item.key(), item.value());
                    	});
                    }

                    page.open();
                    if(loadLocalStorage(accountNameValue, customMap))
                    {
                    	localStorageConfig.setAccount(accountNameValue);

                        if(autoLocalStorage.skipMethod())
                        {
                        	localStorageConfig.setSkipLogin(true);
                        }
                    }
                }
                else
                {
                    throw new AutoTestException("Wrong account type in class: " + accountClz + ", and field : "
                            + accountNameField + ". It should be Text type.");
                }
            }
            
            //加载cookie信息
            boolean skipForCookie = false;
            if(autoCookie != null && PathUtil.isFile(autoCookie.fileName()))
            {
        		// 处理cookie
        		Options manage = util.getEngine().getDriver().manage();
        		File cookieFile = PathUtil.getFile(autoCookie.fileName());
        		
                Class<? extends Page> accountClz = autoCookie.pageClazz();
                String accountNameField = autoCookie.sessionKey();

                Page page = util.getPage(accountClz);
                Field accountField = accountClz.getDeclaredField(accountNameField);

                accountField.setAccessible(true);
                Object value = accountField.get(page);
                if(value instanceof Text)
                {
                    page.open();
                }
        		
    			try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(cookieFile)))
    			{
    				Object cookiesObj = input.readObject();
    				if(cookiesObj != null && cookiesObj instanceof Set<?>)
    				{
    					@SuppressWarnings("unchecked")
						Set<Cookie> cookies =  (Set<Cookie>) cookiesObj;
    					cookies.parallelStream().forEach((cookie) -> {
    						manage.addCookie(cookie);
    					});
    				}
    				
                	skipForCookie = autoCookie.skipMethod();
    			}
    			catch (IOException e)
    			{
    			    logger.error("", e);
    			}
    			catch (ClassNotFoundException e)
    			{
                    logger.error("", e);
    			}
            }

            if(sessionStorageConfig.isSkipLogin() || localStorageConfig.isSkipLogin() || skipForCookie)
            {
                result = Void.TYPE;
            }
            else
            {
                result = methodProxy.invokeSuper(obj, args);
            }

            //保存sessionStorage
            if(sessionStorageConfig.isAutoLoad())
            {
                saveSessionStorage(sessionStorageConfig.getAccount());
            }

            if(localStorageConfig.isAutoLoad())
            {
                saveLocalStorage(localStorageConfig.getAccount());
            }
            
            //保存cookie信息
            if(autoCookie != null)
            {
    			Options manage = util.getEngine().getDriver().manage();
    			File cookieFile = PathUtil.getFile(autoCookie.fileName());
    			
				Set<Cookie> cookies = manage.getCookies();
				try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(cookieFile)))
				{
					output.writeObject(cookies);
				}
				catch (IOException e)
				{
                    logger.error("", e);
				}
            }

            normalRecord.setEndTime(System.currentTimeMillis());

            if(isNotExcludeMethod(method))
            {
                write(normalRecord);
            }
        }
        catch(Exception | AssertionError e)
        {
            boolean acceptException = exceptionHandle(autoExpect, e);

            normalRecord.setEndTime(System.currentTimeMillis());
            write(new ExceptionRecord(e, normalRecord));

            if(acceptException)
            {
                logger.error("", e);
            }
            else
            {
                throw e;
            }
        }

        return result;
    }

    /**
     * 保存sessionStorage信息
     * @param account
     */
    private void saveSessionStorage(String account)
    {
        WebDriver driver = util.getEngine().getDriver();
        if(driver instanceof WebStorage)
        {
            WebStorage webStorage = (WebStorage) driver;
            SessionStorage sessionStorage = webStorage.getSessionStorage();

            Properties pro = new Properties();
            for(String key : sessionStorage.keySet())
            {
                pro.setProperty(key, sessionStorage.getItem(key));
            }

            PathUtil.proStore(pro, "sessionStorage." + account);
        }
    }
    
    private void saveLocalStorage(String account)
    {
        WebDriver driver = util.getEngine().getDriver();
        if(driver instanceof WebStorage)
        {
            WebStorage webStorage = (WebStorage) driver;
            LocalStorage localStorage = webStorage.getLocalStorage();

            Properties pro = new Properties();
            for(String key : localStorage.keySet())
            {
                pro.setProperty(key, localStorage.getItem(key));
            }

            PathUtil.proStore(pro, "localStorage." + account);
        }
    }

    /**
     * 加载sessionStorage信息
     * @param accountNameValue
     * @return
     */
    private boolean loadSessionStorage(String accountNameValue, Map<String, String> customMap)
    {
        WebDriver webDriver = util.getEngine().getDriver();
        if(webDriver instanceof WebStorage)
        {
            WebStorage webStorage = (WebStorage) webDriver;
            SessionStorage sessionStorage = webStorage.getSessionStorage();

            Properties pro = new Properties();
            if(PathUtil.proLoad(pro, "sessionStorage." + accountNameValue))
            {
                if(pro.isEmpty())
                {
                    return false;
                }
                
                pro.putAll(customMap);

                pro.stringPropertyNames().parallelStream().forEach((key) -> {
                    sessionStorage.setItem(key, pro.getProperty(key));
                });

                return true;
            }
        }

        return false;
    }
    
    private boolean loadLocalStorage(String accountNameValue, Map<String, String> customMap)
    {
        WebDriver webDriver = util.getEngine().getDriver();
        if(webDriver instanceof WebStorage)
        {
            WebStorage webStorage = (WebStorage) webDriver;
            LocalStorage localStorage = webStorage.getLocalStorage();

            Properties pro = new Properties();
            if(PathUtil.proLoad(pro, "localStorage." + accountNameValue))
            {
                if(pro.isEmpty())
                {
                    return false;
                }
                
                pro.putAll(customMap);

                pro.stringPropertyNames().parallelStream().forEach((key) -> {
                	localStorage.setItem(key, pro.getProperty(key));
                });

                return true;
            }
        }

        return false;
    }

    /**
     * 根据注解配置，是否要对异常进行处理
     * @param autoExpect
     * @return
     */
    private boolean exceptionHandle(AutoExpect autoExpect, Throwable e)
    {
        if(autoExpect != null)
        {
            Class<?>[] acceptArray = autoExpect.accept();
            if(acceptArray != null && acceptArray.length > 0)
            {
                for(Class<?> clz : acceptArray)
                {
                     if(clz.equals(e.getClass()))
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 被排除的方法
     * @param method
     * @return
     */
    private boolean isNotExcludeMethod(Method method)
    {
        String name = method.getName();
        if("setEngine".equals(name))
        {
            return false;
        }

        if("setWebDriver".equals(name))
        {
            return false;
        }

        return true;
    }

    /**
     * 写入报告
     * @param record
     */
    private void write(NormalRecord record)
    {
        for(RecordReportWriter writer : recordReportWriters)
        {
            writer.write(record);
        }
    }

    /**
     * 写入报告
     * @param record
     */
    private void write(ExceptionRecord record)
    {
    	Map<Object, Object> config = util.getEngine().getEngineConfig();
    	
    	String root = (String) config.get(LoggerConstants.IMG_LOG_DIR);
    	String appDir = (String) config.get(LoggerConstants.APP_IDENTIFY);
    	String progressDir = (String) config.get(LoggerConstants.PROGRESS_IDENTIFY);
    	
    	File pngDir = new File(new File(root, appDir), progressDir);
    	if(pngDir.isDirectory())
    	{
    		File[] files = pngDir.listFiles();
    		if(files != null && files.length > 0)
    		{
        		List<File> attachFileList = new ArrayList<File>();
        		attachFileList.add(files[files.length - 1]);
        		record.setAttachFileList(attachFileList);
    		}
    	}
    	
        for(RecordReportWriter writer : recordReportWriters)
        {
            writer.write(record);
        }
    }
}
