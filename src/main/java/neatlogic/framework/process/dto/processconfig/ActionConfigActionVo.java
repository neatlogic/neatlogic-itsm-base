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

import java.util.List;

/**
 * @author linbq
 * @since 2021/5/20 18:03
 **/
public class ActionConfigActionVo {
    private String integrationUuid;
    private String trigger;
    private String formTag;
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

    public String getFormTag() {
        return formTag;
    }

    public void setFormTag(String formTag) {
        this.formTag = formTag;
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
