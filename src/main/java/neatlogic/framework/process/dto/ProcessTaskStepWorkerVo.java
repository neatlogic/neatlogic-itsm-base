package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.dto.WorkAssignmentUnitVo;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * @Author:chenqiwei
 * @Time:Jun 19, 2019
 * @ClassName: ProcessTaskStepWorkerVo
 * @Description: 记录当前流程任务谁可以处理
 */
public class ProcessTaskStepWorkerVo extends BasePageVo {
    //    @ESKey(type = ESKeyType.PKEY, name = "processTaskId")
    private Long processTaskId;
    private Long processTaskStepId;
    private String type;
    private String uuid;
    private String name;
    private String userType;

    private WorkAssignmentUnitVo worker;
    @JSONField(serialize = false)
    private String newUuid;
    @JSONField(serialize = false)
    private String userUuid;
    @JSONField(serialize = false)
    private List<String> teamUuidList;
    @JSONField(serialize = false)
    private List<String> roleUuidList;
    @JSONField(serialize = false)
    private List<String> userUuidList;

    public ProcessTaskStepWorkerVo() {

    }

    public ProcessTaskStepWorkerVo(Long processTaskId, Long processTaskStepId) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
    }

    public ProcessTaskStepWorkerVo(Long processTaskStepId) {
        this.processTaskStepId = processTaskStepId;
    }

    public ProcessTaskStepWorkerVo(Long processTaskId, Long processTaskStepId, String type, String uuid,
                                   String userType) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.type = type;
        this.uuid = uuid;
        this.userType = userType;
    }

    public ProcessTaskStepWorkerVo(Long processTaskId, Long processTaskStepId, String type, String uuid,
                                   String userType, String newUuid) {
        this.processTaskId = processTaskId;
        this.processTaskStepId = processTaskStepId;
        this.type = type;
        this.uuid = uuid;
        this.userType = userType;
        this.newUuid = newUuid;
    }

    public ProcessTaskStepWorkerVo(Long processTaskStepId, String userType) {
        this.processTaskStepId = processTaskStepId;
        this.userType = userType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((processTaskId == null) ? 0 : processTaskId.hashCode());
        result = prime * result + ((processTaskStepId == null) ? 0 : processTaskStepId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProcessTaskStepWorkerVo other = (ProcessTaskStepWorkerVo) obj;
        if (processTaskId == null) {
            if (other.processTaskId != null)
                return false;
        } else if (!processTaskId.equals(other.processTaskId))
            return false;
        if (processTaskStepId == null) {
            if (other.processTaskStepId != null)
                return false;
        } else if (!processTaskStepId.equals(other.processTaskStepId))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUuid() {
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

    public String getWorkerValue() {
        GroupSearch groupSearch = GroupSearch.getGroupSearch(this.type);
        if (groupSearch != null) {
            return groupSearch.getValuePlugin() + this.uuid;
        }
        return null;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getNewUuid() {
        return newUuid;
    }

    public void setNewUuid(String newUuid) {
        this.newUuid = newUuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public List<String> getTeamUuidList() {
        return teamUuidList;
    }

    public void setTeamUuidList(List<String> teamUuidList) {
        this.teamUuidList = teamUuidList;
    }

    public List<String> getRoleUuidList() {
        return roleUuidList;
    }

    public void setRoleUuidList(List<String> roleUuidList) {
        this.roleUuidList = roleUuidList;
    }

    public WorkAssignmentUnitVo getWorker() {
        return worker;
    }

    public void setWorker(WorkAssignmentUnitVo worker) {
        this.worker = worker;
    }

    public List<String> getUserUuidList() {
        return userUuidList;
    }

    public void setUserUuidList(List<String> userUuidList) {
        this.userUuidList = userUuidList;
    }
}
