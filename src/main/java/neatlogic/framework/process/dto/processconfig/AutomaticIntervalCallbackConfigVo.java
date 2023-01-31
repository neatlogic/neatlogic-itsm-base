/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto.processconfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/5/20 10:40
 **/
public class AutomaticIntervalCallbackConfigVo {
    private Integer interval;
    private String integrationUuid;
    private String resultTemplate;
    private List<IntegrationParamMappingVo> paramList = new ArrayList<>();
    private AssertionConfigVo successConfig;
    private AssertionConfigVo failConfig;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getIntegrationUuid() {
        return integrationUuid;
    }

    public void setIntegrationUuid(String integrationUuid) {
        this.integrationUuid = integrationUuid;
    }

    public String getResultTemplate() {
        return resultTemplate;
    }

    public void setResultTemplate(String resultTemplate) {
        this.resultTemplate = resultTemplate;
    }

    public List<IntegrationParamMappingVo> getParamList() {
        return paramList;
    }

    public void setParamList(List<IntegrationParamMappingVo> paramList) {
        this.paramList = paramList;
    }

    public AssertionConfigVo getSuccessConfig() {
        return successConfig;
    }

    public void setSuccessConfig(AssertionConfigVo successConfig) {
        this.successConfig = successConfig;
    }

    public AssertionConfigVo getFailConfig() {
        return failConfig;
    }

    public void setFailConfig(AssertionConfigVo failConfig) {
        this.failConfig = failConfig;
    }
}
