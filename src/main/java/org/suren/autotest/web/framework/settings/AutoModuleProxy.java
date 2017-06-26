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

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.suren.autotest.web.framework.annotation.AutoCookie;
import org.suren.autotest.web.framework.annotation.AutoExpect;
import org.suren.autotest.web.framework.annotation.AutoModule;
import org.suren.autotest.web.framework.annotation.AutoSessionStorage;
import org.suren.autotest.web.framework.core.AutoTestException;
import org.suren.autotest.web.framework.core.ui.Text;
import org.suren.autotest.web.framework.page.Page;
import org.suren.autotest.web.framework.report.RecordReportWriter;
import org.suren.autotest.web.framework.report.record.ExceptionRecord;
import org.suren.autotest.web.framework.report.record.NormalRecord;
import org.suren.autotest.web.framework.util.PathUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * 模块代理类
 * @author suren
 */
public class AutoModuleProxy implements MethodInterceptor
{
    private Enhancer enhancer = new Enhancer();
    private Object target;
    private List<RecordReportWriter> recordReportWriters;
    private SettingUtil util;

    public AutoModuleProxy(Object target, List<RecordReportWriter> recordReportWriters, SettingUtil util)
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

                    page.open();
                    if(loadSessionStorage(accountNameValue))
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
            
            //加载cookie信息
            boolean skipForCookie = false;
            if(autoCookie != null && PathUtil.isFile(autoCookie.fileName()))
            {
        		// 处理cookie
        		Options manage = util.getEngine().getDriver().manage();
        		File cookieFile = PathUtil.getFile(autoCookie.fileName());
        		
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
    				e.printStackTrace();
    			}
    			catch (ClassNotFoundException e)
    			{
    				e.printStackTrace();
    			}
            }

            if(sessionStorageConfig.isSkipLogin() || skipForCookie)
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
					e.printStackTrace();
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
                e.printStackTrace();
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

    /**
     * 加载sessionStorage信息
     * @param accountNameValue
     * @return
     */
    private boolean loadSessionStorage(String accountNameValue)
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

                pro.stringPropertyNames().parallelStream().forEach((key) -> {
                    sessionStorage.setItem(key, pro.getProperty(key));
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

    private void write(NormalRecord record)
    {
        for(RecordReportWriter writer : recordReportWriters)
        {
            writer.write(record);
        }
    }

    private void write(ExceptionRecord record)
    {
        for(RecordReportWriter writer : recordReportWriters)
        {
            writer.write(record);
        }
    }
}
