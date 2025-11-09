/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
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
