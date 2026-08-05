/*Copyright (C) 2023  深圳极向量科技有限公司 All Rights Reserved.

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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/1 12:11
 **/
public class TaskConfigVo extends BaseEditorVo {
    private static final long serialVersionUID = -6035457128604271114L;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.id.name", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.name.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.num.name", type = ApiParamType.STRING)
    private Integer num;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.policy.name", type = ApiParamType.STRING)
    private String policy;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.isactive.name", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.referencecount.name", type = ApiParamType.INTEGER)
    private Integer referenceCount;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.processtasksteptasklist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepTaskVo> processTaskStepTaskList;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.rangelist.name", type = ApiParamType.JSONARRAY)
    private List<String> rangeList;
    @EntityField(name = "nfpd.taskconfigvo.entityfield.config.name", type = ApiParamType.JSONOBJECT)
    private JSONObject config;
    @JSONField(serialize = false)
    private String configStr;

    public Long getId() {
        if(id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(Integer referenceCount) {
        this.referenceCount = referenceCount;
    }

    public List<ProcessTaskStepTaskVo> getProcessTaskStepTaskList() {
        return processTaskStepTaskList;
    }

    public void setProcessTaskStepTaskList(List<ProcessTaskStepTaskVo> processTaskStepTaskList) {
        this.processTaskStepTaskList = processTaskStepTaskList;
    }

    public List<String> getRangeList() {
        return rangeList;
    }

    public void setRangeList(List<String> rangeList) {
        this.rangeList = rangeList;
    }

    public JSONObject getConfig() {
        if (config == null && StringUtils.isNotBlank(configStr)) {
            config = JSONObject.parseObject(configStr);
        }
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }

    public String getConfigStr() {
        if (configStr == null && config != null) {
            configStr = config.toJSONString();
        }
        return configStr;
    }

    public void setConfigStr(String configStr) {
        this.configStr = configStr;
    }
}
