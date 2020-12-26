package org.suren.autotest.web.framework.settings;

import java.util.HashMap;

/**
 * This is very simple cache. We should replace it in the future.
 */
public class Cache extends HashMap {
    private static final Cache cache  =new Cache();
    public static Cache getInstance() {
        return cache;
    }
}
