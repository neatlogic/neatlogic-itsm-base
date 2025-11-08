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

package neatlogic.framework.process.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;

import java.util.List;

public class ProcessVo extends BaseEditorVo {

    @EntityField(name = "common.uuid", type = ApiParamType.STRING)
    private String uuid;

    @EntityField(name = "common.name", type = ApiParamType.STRING)
    private String name;

//    @EntityField(name = "common.typename", type = ApiParamType.STRING)
//    private String typeName;

    @EntityField(name = "common.isactive", type = ApiParamType.INTEGER)
    private Integer isActive;

    @EntityField(name = "common.config", type = ApiParamType.JSONOBJECT)
    private JSONObject config;

    @EntityField(name = "common.referencecount", type = ApiParamType.INTEGER)
    private int referenceCount;
    // @EntityField(name = "流程表单uuid", type = ApiParamType.STRING)
    private String formUuid;
    @JSONField(serialize = false)
    private List<ProcessStepVo> stepList;
//    @JSONField(serialize = false)
//    private List<ProcessStepRelVo> stepRelList;
//    @JSONField(serialize = false)
//    private List<ProcessSlaVo> slaList;
//    @JSONField(serialize = false)
//    private ProcessScoreTemplateVo processScoreTemplateVo;
//    @JSONField(serialize = false)
//    private InvokeNotifyPolicyConfigVo notifyPolicyConfig;
//    @JSONField(serialize = false)
//    private List<String> integrationUuidList = new ArrayList<>();

    @JSONField(serialize = false)
    private String configStr;

    public synchronized String getUuid() {
//        if (StringUtils.isBlank(uuid)) {
//            uuid = UUID.randomUUID().toString().replace("-", "");
//        }
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(String typeName) {
//        this.typeName = typeName;
//    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public JSONObject getConfig() {
        if (config == null && configStr != null) {
            config = JSON.parseObject(configStr);
        }
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public List<ProcessStepVo> getStepList() {
        return stepList;
    }

    public void setStepList(List<ProcessStepVo> stepList) {
        this.stepList = stepList;
    }

//    public List<ProcessStepRelVo> getStepRelList() {
//        return stepRelList;
//    }
//
//    public void setStepRelList(List<ProcessStepRelVo> stepRelList) {
//        this.stepRelList = stepRelList;
//    }

    public String getFormUuid() {
        return formUuid;
    }

    public void setFormUuid(String formUuid) {
        this.formUuid = formUuid;
    }


    public int getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(int referenceCount) {
        this.referenceCount = referenceCount;
    }

//    public List<ProcessSlaVo> getSlaList() {
//        return slaList;
//    }
//
//    public void setSlaList(List<ProcessSlaVo> slaList) {
//        this.slaList = slaList;
//    }

//    public ProcessScoreTemplateVo getProcessScoreTemplateVo() {
//        return processScoreTemplateVo;
//    }
//
//    public void setProcessScoreTemplateVo(ProcessScoreTemplateVo processScoreTemplateVo) {
//        this.processScoreTemplateVo = processScoreTemplateVo;
//    }

//    public InvokeNotifyPolicyConfigVo getNotifyPolicyConfig() {
//        return notifyPolicyConfig;
//    }
//
//    public void setNotifyPolicyConfig(InvokeNotifyPolicyConfigVo notifyPolicyConfig) {
//        this.notifyPolicyConfig = notifyPolicyConfig;
//    }

//    public List<String> getIntegrationUuidList() {
//        return integrationUuidList;
//    }
//
//    public void setIntegrationUuidList(List<String> integrationUuidList) {
//        this.integrationUuidList = integrationUuidList;
//    }

    public String getConfigStr() {
        if (config != null) {
            return config.toJSONString();
        }
        return configStr;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }
}
