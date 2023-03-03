/*
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
