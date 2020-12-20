package org.suren.autotest.web.framework.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suren.autotest.web.framework.selenium.SeleniumEngine;
import org.suren.autotest.web.framework.settings.Phoenix;
import org.suren.autotest.web.framework.settings.PhoenixParam;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RunnerStartup implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(RunnerStartup.class);

    private Phoenix phoenix;

    public void start(Class<?> ... annotatedClasses) {
        phoenix = new Phoenix(annotatedClasses);
        phoenix.initWithoutDriver(new PhoenixParam());
        phoenix.initData();

        SeleniumEngine engine = phoenix.getEngine();
        engine.setDriverStr("chrome");
        engine.init();
        logger.info("phoenix init done");

        Map<String, Runner> runners = phoenix.getRunner(Runner.class);
        logger.info("found " + runners.size() + " runners");
        runners.forEach((k, v) -> {
            System.out.println(k + v);

            Method[] methods = v.getClass().getMethods();
            for (Method m : methods) {
                if (m.getParameterCount() != 0
                || m.getDeclaringClass().equals(Object.class)) {
                    continue;
                }

                try {
                    m.invoke(v);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void close() throws IOException {
        phoenix.close();
        phoenix.shutdown();
    }
}
