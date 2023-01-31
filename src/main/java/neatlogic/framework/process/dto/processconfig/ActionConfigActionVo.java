/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dto.processconfig;

import java.util.List;

/**
 * @author linbq
 * @since 2021/5/20 18:03
 **/
public class ActionConfigActionVo {
    private String integrationUuid;
    private String trigger;
    private AssertionConfigVo successCondition;
    private List<IntegrationParamMappingVo> paramMappingList;

    public String getIntegrationUuid() {
        return integrationUuid;
    }

    public void setIntegrationUuid(String integrationUuid) {
        this.integrationUuid = integrationUuid;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public AssertionConfigVo getSuccessCondition() {
        return successCondition;
    }

    public void setSuccessCondition(AssertionConfigVo successCondition) {
        this.successCondition = successCondition;
    }

    public List<IntegrationParamMappingVo> getParamMappingList() {
        return paramMappingList;
    }

    public void setParamMappingList(List<IntegrationParamMappingVo> paramMappingList) {
        this.paramMappingList = paramMappingList;
    }
}
