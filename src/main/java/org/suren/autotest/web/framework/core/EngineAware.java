package org.suren.autotest.web.framework.core;

import org.suren.autotest.web.framework.settings.SettingUtil;

/**
 * 引擎感知接口
 * @author suren
 */
public interface EngineAware
{
    /**
     * 通过该接口能获取的selenium的原始api
     * @param util
     */
    void setEngine(SettingUtil util);
}
