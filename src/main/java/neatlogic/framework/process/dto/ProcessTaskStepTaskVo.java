/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

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
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.process.constvalue.task.TaskConfigPolicy;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lvzk
 * @since 2021/8/31 11:03
 **/
public class ProcessTaskStepTaskVo implements Serializable {
    private static final long serialVersionUID = -6616612590947765111L;
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
    @EntityField(name = "任务策略", type = ApiParamType.STRING)
    private String taskConfigPolicy;
    @EntityField(name = "任务策略名", type = ApiParamType.STRING)
    private String taskConfigPolicyName;
    @EntityField(name = "创建人uuid", type = ApiParamType.STRING)
    private String owner;
    @EntityField(name = "创建人")
    private UserVo ownerVo;
//    @EntityField(name = "状态", type = ApiParamType.STRING)
//    private String status;
//    @EntityField(name = "状态信息", type = ApiParamType.JSONOBJECT)
//    private ProcessTaskStatusVo statusVo;
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
    private List<String> userList;
    @EntityField(name = "处理人VoList", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepTaskUserVo> stepTaskUserVoList = new ArrayList<>();
    @JSONField(serialize = false)
    private JSONObject paramObj;
    //用户回复参数，目前用于邮件回复内容
    private String taskStepTaskUserContent;

    public ProcessTaskStepTaskVo() {
    }

    public ProcessTaskStepTaskVo(Long processtaskStepTaskId) {
        this.id = processtaskStepTaskId;
    }

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

//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }

//    public ProcessTaskStatusVo getStatusVo() {
//        if (statusVo == null && StringUtils.isNotBlank(status)) {
//            statusVo = new ProcessTaskStatusVo(status);
//        }
//        return statusVo;
//    }
//
//    public void setStatusVo(ProcessTaskStatusVo statusVo) {
//        this.statusVo = statusVo;
//    }

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

    public List<String> getUserList() {
        if (CollectionUtils.isNotEmpty(userList)) {
            for (int i = 0; i < userList.size(); i++) {
                userList.set(i, userList.get(i).replaceAll(GroupSearch.USER.getValuePlugin(), StringUtils.EMPTY));
            }
        }
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public JSONObject getParamObj() {
        if (MapUtils.isEmpty(paramObj)) {
            paramObj = new JSONObject();
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

    public String getTaskConfigPolicy() {
        return taskConfigPolicy;
    }

    public void setTaskConfigPolicy(String taskConfigPolicy) {
        this.taskConfigPolicy = taskConfigPolicy;
    }

    public String getTaskConfigPolicyName() {
        if (StringUtils.isNotBlank(taskConfigPolicy)) {
            taskConfigPolicyName = TaskConfigPolicy.getName(taskConfigPolicy);
        }
        return taskConfigPolicyName;
    }

    public void setTaskConfigPolicyName(String taskConfigPolicyName) {
        this.taskConfigPolicyName = taskConfigPolicyName;
    }

    public String getTaskStepTaskUserContent() {
        return taskStepTaskUserContent;
    }

    public void setTaskStepTaskUserContent(String taskStepTaskUserContent) {
        this.taskStepTaskUserContent = taskStepTaskUserContent;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
