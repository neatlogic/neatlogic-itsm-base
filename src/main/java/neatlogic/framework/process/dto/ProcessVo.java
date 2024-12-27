/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
