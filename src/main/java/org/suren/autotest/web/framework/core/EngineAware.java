package org.suren.autotest.web.framework.core;

import org.suren.autotest.web.framework.settings.SettingUtil;

/**
 * 引擎感知接口
 * @author suren
 */
public interface EngineAware
{
    void setEngine(SettingUtil util);
}
