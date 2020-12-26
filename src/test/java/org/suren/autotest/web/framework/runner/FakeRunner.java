package org.suren.autotest.web.framework.runner;

import org.suren.autotest.web.framework.annotation.AutoRunner;

@AutoRunner
public class FakeRunner implements Runner {
    public void test() {
        System.out.println("dddd");
    }
}
