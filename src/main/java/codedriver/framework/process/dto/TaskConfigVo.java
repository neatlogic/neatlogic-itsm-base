/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;
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
    @EntityField(name = "id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "任务名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "参与人数。-1：不做限制", type = ApiParamType.STRING)
    private Integer num;
    @EntityField(name = "其中一个人完成即可：any,所有人完成：all", type = ApiParamType.STRING)
    private String policy;
    @EntityField(name = "是否激活", type = ApiParamType.INTEGER)
    private Integer isActive;
    @EntityField(name = "依赖数", type = ApiParamType.INTEGER)
    private Integer referenceCount;
    @EntityField(name = "任务列表", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepTaskVo> processTaskStepTaskList;
    @EntityField(name = "处理人过滤范围", type = ApiParamType.JSONARRAY)
    private List<String> rangeList;
    @EntityField(name = "配置信息", type = ApiParamType.JSONOBJECT)
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
