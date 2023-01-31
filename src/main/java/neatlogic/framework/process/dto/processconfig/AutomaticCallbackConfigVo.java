/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto.processconfig;

/**
 * @author linbq
 * @since 2021/5/20 10:23
 **/
public class AutomaticCallbackConfigVo {
    private String type;
    private AutomaticIntervalCallbackConfigVo config;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AutomaticIntervalCallbackConfigVo getConfig() {
        return config;
    }

    public void setConfig(AutomaticIntervalCallbackConfigVo config) {
        this.config = config;
    }
}
