/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.dto.UserVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lvzk
 * @since 2021/8/31 11:03
 **/
public class ProcessTaskStepTaskVo {
    @EntityField(name = "工单id", type = ApiParamType.LONG)
    private Long processTaskId;
    @EntityField(name = "步骤id", type = ApiParamType.LONG)
    private Long processTaskStepId;
    @EntityField(name = "任务id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "任务配置id", type = ApiParamType.LONG)
    private Long taskConfigId;
    @EntityField(name = "任务配置", type = ApiParamType.STRING)
    private String taskConfigName;
    @EntityField(name = "创建人")
    private UserVo ownerVo;
    @EntityField(name = "状态", type = ApiParamType.STRING)
    private String status;
    @EntityField(name = "状态信息", type = ApiParamType.JSONOBJECT)
    private ProcessTaskStatusVo statusVo;
    @EntityField(name = "创建时间", type = ApiParamType.LONG)
    private Date createTime;
    @EntityField(name = "结束时间", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "描述", type = ApiParamType.STRING)
    private String content;
    @EntityField(name = "描述hash值", type = ApiParamType.STRING)
    private String contentHash;
    @EntityField(name = "步骤主处理人", type = ApiParamType.STRING)
    private String majorUser;
    @EntityField(name = "处理人", type = ApiParamType.JSONARRAY)
    private JSONArray userList;
    @EntityField(name = "处理人VoList", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepTaskUserVo> stepTaskUserVoList = new ArrayList<>();
    @JSONField(serialize = false)
    private JSONObject paramObj;

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getProcessTaskStepId() {
        return processTaskStepId;
    }

    public void setProcessTaskStepId(Long processTaskStepId) {
        this.processTaskStepId = processTaskStepId;
    }

    public Long getId() {
        if (id == null) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserVo getOwnerVo() {
        return ownerVo;
    }

    public void setOwnerVo(UserVo ownerVo) {
        this.ownerVo = ownerVo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProcessTaskStatusVo getStatusVo() {
        if (statusVo == null && StringUtils.isNotBlank(status)) {
            statusVo = new ProcessTaskStatusVo(status);
        }
        return statusVo;
    }

    public void setStatusVo(ProcessTaskStatusVo statusVo) {
        this.statusVo = statusVo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public String getMajorUser() {
        return majorUser;
    }

    public void setMajorUser(String majorUser) {
        this.majorUser = majorUser;
    }

    public JSONArray getUserList() {
        if (CollectionUtils.isNotEmpty(userList)) {
            for (int i = 0; i < userList.size(); i++) {
                userList.set(i, userList.get(i).toString().replaceAll(GroupSearch.USER.getValuePlugin(), StringUtils.EMPTY));
            }
        }
        return userList;
    }

    public void setUserList(JSONArray userList) {
        this.userList = userList;
    }

    public JSONObject getParamObj() {
        if (MapUtils.isEmpty(paramObj)) {
            return new JSONObject();
        }
        return paramObj;
    }

    public void setParamObj(JSONObject paramObj) {
        this.paramObj = paramObj;
    }

    public Long getTaskConfigId() {
        return taskConfigId;
    }

    public void setTaskConfigId(Long taskConfigId) {
        this.taskConfigId = taskConfigId;
    }

    public String getTaskConfigName() {
        return taskConfigName;
    }

    public void setTaskConfigName(String taskConfigName) {
        this.taskConfigName = taskConfigName;
    }

    public List<ProcessTaskStepTaskUserVo> getStepTaskUserVoList() {
        return stepTaskUserVoList;
    }

    public void setStepTaskUserVoList(List<ProcessTaskStepTaskUserVo> stepTaskUserVoList) {
        this.stepTaskUserVoList = stepTaskUserVoList;
    }
}
