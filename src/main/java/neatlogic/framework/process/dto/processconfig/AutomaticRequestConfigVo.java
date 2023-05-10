/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

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

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/5/20 10:22
 **/
public class AutomaticRequestConfigVo {
    private String failPolicy = "";
    private String integrationUuid = "";
    private String resultTemplate = "";
    private List<IntegrationParamMappingVo> paramList = new ArrayList<>();
    private AssertionConfigVo successConfig = new AssertionConfigVo();

    public String getFailPolicy() {
        return failPolicy;
    }

    public void setFailPolicy(String failPolicy) {
        this.failPolicy = failPolicy;
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
}
