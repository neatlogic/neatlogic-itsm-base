package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.form.dto.FormAttributeVo;
import neatlogic.framework.process.constvalue.ProcessTaskStepStatus;
import neatlogic.framework.process.stephandler.core.ProcessStepInternalHandlerFactory;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ProcessTaskStepVo extends BasePageVo {

    @EntityField(name = "nfpd.processtaskstepvo.entityfield.id.name", type = ApiParamType.LONG)
    private Long id;
    //	@ESKey(type = ESKeyType.PKEY, name ="processTaskId")
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.processtaskid.name", type = ApiParamType.LONG)
    private Long processTaskId;
    /**
     * 前置步骤id
     */
    @JSONField(serialize = false)
    private Long fromProcessTaskStepId;
    /**
     * 发起操作步骤id
     */
    @JSONField(serialize = false)
    private Long startProcessTaskStepId;
    private String processUuid;
    private String processStepUuid;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.name.name", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.aliasname.name", type = ApiParamType.STRING)
    private String aliasName;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.status.name", type = ApiParamType.STRING)
    private String status;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.statusvo.name", type = ApiParamType.JSONOBJECT)
    private ProcessTaskStepStatusVo statusVo;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.handler.name", type = ApiParamType.STRING)
    private String handler;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.type.name", type = ApiParamType.STRING)
    private String type;
//    private String formUuid;
    private Integer isActive = 0;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.activetime.name", type = ApiParamType.LONG)
    private Date activeTime;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.starttime.name", type = ApiParamType.LONG)
    private Date startTime;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.endtime.name", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.expiretime.name", type = ApiParamType.LONG)
    private Date expireTime;
    private Long expireTimeLong;
    private String error;
    private String result;
    private String configHash;
    private String taskConfigHash;// 工单配置hash
    private Boolean isAllDone = false;
    private Boolean isCurrentUserDone = false;
    private Boolean isWorkerPolicyListSorted = false;
    //@EntityField(name = "nfpd.processtaskstepvo.entityfield.userlist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepUserVo> userList = new ArrayList<>();
    @JSONField(serialize = false)
    private List<ProcessTaskStepRelVo> relList = new ArrayList<>();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.workerlist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepWorkerVo> workerList = new ArrayList<>();
    private List<ProcessTaskStepWorkerPolicyVo> workerPolicyList = new ArrayList<>();
//    private List<ProcessTaskStepFormAttributeVo> formAttributeList = new ArrayList<>();
    private List<FormAttributeVo> formAttributeVoList = new ArrayList<>();

    @JSONField(serialize = false)
    private final JSONObject paramObj = new JSONObject();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.majoruser.name", type = ApiParamType.JSONOBJECT)
    private ProcessTaskStepUserVo majorUser;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.minoruserlist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepUserVo> minorUserList = new ArrayList<>();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.comment.name", type = ApiParamType.JSONOBJECT)
    private ProcessTaskStepReplyVo comment;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.commentlist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepReplyVo> commentList = new ArrayList<>();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.actionlist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskActionVo> actionList = new ArrayList<>();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.isneeduploadfile.name", type = ApiParamType.INTEGER)
    private Integer isNeedUploadFile;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.isneedcontent.name", type = ApiParamType.INTEGER)
    private Integer isNeedContent;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.contenthelp.name", type = ApiParamType.STRING)
    private String contentHelp;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.isrequired.name", type = ApiParamType.INTEGER)
    private Integer isRequired;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.isallowprocessonmobile.name", type = ApiParamType.INTEGER)
    private Integer isAllowProcessOnMobile;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.enablereapproval.name", type = ApiParamType.INTEGER)
    private Integer enableReapproval;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.formsceneuuid.name", type = ApiParamType.STRING)
    private String formSceneUuid;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.replaceabletextlist.name", type = ApiParamType.JSONARRAY)
    private JSONArray replaceableTextList;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.custombuttonlist.name", type = ApiParamType.JSONARRAY)
    private JSONArray customButtonList;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.customstatuslist.name", type = ApiParamType.JSONARRAY)
    private JSONArray customStatusList;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.flowdirection.name", type = ApiParamType.STRING)
    private String flowDirection;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.processtasksteptask.name", type = ApiParamType.JSONOBJECT)
    @Deprecated
    private JSONObject processTaskStepTask = new JSONObject();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.taskconfiglist.name", type = ApiParamType.JSONARRAY)
    private List<TaskConfigVo> taskConfigList;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.processtasksteptaskvo.name", type = ApiParamType.JSONOBJECT)
    private ProcessTaskStepTaskVo processTaskStepTaskVo;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.isview.name", type = ApiParamType.INTEGER)
    private Integer isView;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.assignableworkersteplist.name", type = ApiParamType.JSONARRAY)
    private List<AssignableWorkerStepVo> assignableWorkerStepList = new ArrayList<>();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.slatimelist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskSlaTimeVo> slaTimeList = new ArrayList<>();
    @JSONField(serialize = false)
    private Boolean isAutoGenerateId = false;

    @EntityField(name = "nfpd.processtaskstepvo.entityfield.processtaskstepdata.name", type = ApiParamType.JSONOBJECT)
    private JSONObject processTaskStepData;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.handlerstepinfo.name", type = ApiParamType.JSONOBJECT)
    private Object handlerStepInfo;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.forwardnextsteplist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepVo> forwardNextStepList = new ArrayList<>();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.backwardnextsteplist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepVo> backwardNextStepList = new ArrayList<>();

    @EntityField(name = "nfpd.processtaskstepvo.entityfield.processtaskstepremindlist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepRemindVo> processTaskStepRemindList = new ArrayList<>();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.originaluser.name", type = ApiParamType.STRING)
    private String originalUser;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.originaluservo.name")
    private UserVo originalUserVo;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.processtaskstepagentvo.name", type = ApiParamType.JSONOBJECT)
    private ProcessTaskStepAgentVo processTaskStepAgentVo;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.commenttemplate.name", type = ApiParamType.JSONOBJECT)
    private ProcessCommentTemplateVo commentTemplate;
    @JSONField(serialize = false)
    private int updateActiveTime;
    @JSONField(serialize = false)
    private int updateStartTime;
    @JSONField(serialize = false)
    private int updateEndTime;
    @JSONField(serialize = false)
    private String nextStepName;// 下一步骤名称
    @JSONField(serialize = false)
    private Long nextStepId;// 下一步骤id
    /**
     * 并行激活节点
     */
    @JSONField(serialize = false)
    private List<Long> parallelActivateStepIdList = new ArrayList<>();
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.processtaglist.name", type = ApiParamType.JSONARRAY)
    private List<ProcessTagVo> processTagList;

    @JSONField(serialize = false)
    private Long subProcessTaskId; //子工单id

    @EntityField(name = "nfpd.processtaskstepvo.entityfield.viewprevnodeuuidlist.name",type= ApiParamType.JSONARRAY)
    private List<String> viewPrevNodeUuidList;

    @EntityField(name = "nfpd.processtaskstepvo.entityfield.viewprevnodelist.name",type= ApiParamType.JSONARRAY)
    private List<ProcessTaskStepVo> viewPrevNodeList;

    @EntityField(name = "nfpd.processtaskstepvo.entityfield.isinthecurrentsteptab.name", type = ApiParamType.INTEGER)
    private Integer isInTheCurrentStepTab;
    @EntityField(name = "nfpd.processtaskstepvo.entityfield.config.name", type = ApiParamType.JSONOBJECT)
    private JSONObject config;

    public ProcessTaskStepVo() {

    }

    public ProcessTaskStepVo(ProcessStepVo processStepVo) {
        this.isAutoGenerateId = true;
        this.setProcessUuid(processStepVo.getProcessUuid());
        this.setProcessStepUuid(processStepVo.getUuid());
        this.setName(processStepVo.getName());
        this.setHandler(processStepVo.getHandler());
        this.setType(processStepVo.getType());
//        this.setFormUuid(processStepVo.getFormUuid());

//        if (processStepVo.getFormAttributeList() != null && processStepVo.getFormAttributeList().size() > 0) {
//            List<ProcessTaskStepFormAttributeVo> attributeList = new ArrayList<>();
//            for (ProcessStepFormAttributeVo attributeVo : processStepVo.getFormAttributeList()) {
//                attributeVo.setProcessStepUuid(processStepVo.getUuid());
//                ProcessTaskStepFormAttributeVo processTaskStepAttributeVo = new ProcessTaskStepFormAttributeVo(attributeVo);
//                attributeList.add(processTaskStepAttributeVo);
//            }
//            this.setFormAttributeList(attributeList);
//        }
        if (processStepVo.getWorkerPolicyList() != null && processStepVo.getWorkerPolicyList().size() > 0) {
            List<ProcessTaskStepWorkerPolicyVo> policyList = new ArrayList<>();
            for (ProcessStepWorkerPolicyVo policyVo : processStepVo.getWorkerPolicyList()) {
                policyVo.setProcessStepUuid(processStepVo.getUuid());
                ProcessTaskStepWorkerPolicyVo processTaskStepWorkerPolicyVo = new ProcessTaskStepWorkerPolicyVo(policyVo);
                policyList.add(processTaskStepWorkerPolicyVo);
            }
            this.setWorkerPolicyList(policyList);
        }

    }

    public ProcessTaskStepVo(Long id, ProcessTaskStepStatus status, Integer isActive) {
        this.id = id;
        this.status = status.getValue();
        this.isActive = isActive;
    }

    public ProcessTaskStepVo(Long processTaskId, String name) {
        this.processTaskId = processTaskId;
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (!(other instanceof ProcessTaskStepVo))
            return false;

        final ProcessTaskStepVo step = (ProcessTaskStepVo) other;
        try {
            if (getId() != null && getId().equals(step.getId())) {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (getId() != null) {
            result += getId().hashCode() * 37;
        }
        return result;
    }

    public synchronized Long getId() {
        if (id == null && isAutoGenerateId) {
            id = SnowflakeUtil.uniqueLong();
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProcessTaskStepStatusVo getStatusVo() {
        if (statusVo == null && StringUtils.isNotBlank(status) && StringUtils.isNotBlank(configHash) && StringUtils.isNotBlank(handler)) {
            String statusText = ProcessStepInternalHandlerFactory.getHandler().getStatusTextByConfigHashAndHandler(configHash, handler, status);
            if (StringUtils.isNotBlank(statusText)) {
                statusVo = new ProcessTaskStepStatusVo(status, statusText);
            } else {
                statusVo = new ProcessTaskStepStatusVo(status);
            }
        }
        return statusVo;
    }

    public void setStatusVo(ProcessTaskStepStatusVo statusVo) {
        this.statusVo = statusVo;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActived) {
        this.isActive = isActived;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<ProcessTaskStepUserVo> getUserList() {
        return userList;
    }

    public void setUserList(List<ProcessTaskStepUserVo> userList) {
        this.userList = userList;
    }

    public Integer getIsRequired() {
        if (isRequired == null && StringUtils.isNotBlank(configHash)) {
            isRequired = ProcessStepInternalHandlerFactory.getHandler().getIsRequiredByConfigHash(configHash);
        }
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public Integer getIsAllowProcessOnMobile() {
        if (isAllowProcessOnMobile == null && StringUtils.isNotBlank(configHash)) {
            isAllowProcessOnMobile = ProcessStepInternalHandlerFactory.getHandler().getIsAllowProcessOnMobileByConfigHash(configHash);
        }
        return isAllowProcessOnMobile;
    }

    public void setIsAllowProcessOnMobile(Integer isAllowProcessOnMobile) {
        this.isAllowProcessOnMobile = isAllowProcessOnMobile;
    }

    public Integer getIsNeedUploadFile() {
        if (isNeedUploadFile == null && StringUtils.isNotBlank(configHash)) {
            isNeedUploadFile = ProcessStepInternalHandlerFactory.getHandler().getIsNeedUploadFileByConfigHash(configHash);
        }
        return isNeedUploadFile;
    }

    public Integer getIsNeedContent() {
        if (isNeedContent == null && StringUtils.isNotBlank(configHash)) {
            isNeedContent = ProcessStepInternalHandlerFactory.getHandler().getIsNeedContentByConfigHash(configHash);
        }
        return isNeedContent;
    }

    public String getContentHelp() {
        return contentHelp;
    }

    public void setContentHelp(String contentHelp) {
        this.contentHelp = contentHelp;
    }

    public Integer getEnableReapproval() {
        if (enableReapproval == null && StringUtils.isNotBlank(configHash)) {
            enableReapproval = ProcessStepInternalHandlerFactory.getHandler().getEnableReapprovalByConfigHash(configHash);
        }
        return enableReapproval;
    }

    public void setEnableReapproval(Integer enableReapproval) {
        this.enableReapproval = enableReapproval;
    }

    public String getFormSceneUuid() {
        if (formSceneUuid == null && StringUtils.isNotBlank(configHash)) {
            formSceneUuid = ProcessStepInternalHandlerFactory.getHandler().getFormSceneUuidByConfigHash(configHash);
        }
        return formSceneUuid;
    }

    public void setFormSceneUuid(String formSceneUuid) {
        this.formSceneUuid = formSceneUuid;
    }

    public JSONArray getReplaceableTextList() {
        return replaceableTextList;
    }

    public void setReplaceableTextList(JSONArray replaceableTextList) {
        this.replaceableTextList = replaceableTextList;
    }

    public JSONArray getCustomButtonList() {
        return customButtonList;
    }

    public void setCustomButtonList(JSONArray customButtonList) {
        this.customButtonList = customButtonList;
    }

    public JSONArray getCustomStatusList() {
        return customStatusList;
    }

    public void setCustomStatusList(JSONArray customStatusList) {
        this.customStatusList = customStatusList;
    }

    public void setIsNeedUploadFile(Integer isNeedUploadFile) {
        this.isNeedUploadFile = isNeedUploadFile;
    }

    public void setIsNeedContent(Integer isNeedContent) {
        this.isNeedContent = isNeedContent;
    }

    public String getFlowDirection() {
        return flowDirection;
    }

    public void setFlowDirection(String flowDirection) {
        this.flowDirection = flowDirection;
    }

    public List<ProcessTaskStepRelVo> getRelList() {
        return relList;
    }

    public void setRelList(List<ProcessTaskStepRelVo> relList) {
        this.relList = relList;
    }

    public List<ProcessTaskStepWorkerVo> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(List<ProcessTaskStepWorkerVo> workerList) {
        this.workerList = workerList;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getProcessStepUuid() {
        return processStepUuid;
    }

    public void setProcessStepUuid(String processStepUuid) {
        this.processStepUuid = processStepUuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcessUuid() {
        return processUuid;
    }

    public void setProcessUuid(String processUuid) {
        this.processUuid = processUuid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void appendError(String error) {
        if (this.error != null) {
            this.error += "\n";
            this.error += error;
        } else {
            this.error = error;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getFromProcessTaskStepId() {
        return fromProcessTaskStepId;
    }

    public void setFromProcessTaskStepId(Long fromProcessTaskStepId) {
        this.fromProcessTaskStepId = fromProcessTaskStepId;
    }

    public List<ProcessTaskStepWorkerPolicyVo> getWorkerPolicyList() {
        if (!isWorkerPolicyListSorted && workerPolicyList != null && workerPolicyList.size() > 0) {
            Collections.sort(workerPolicyList);
            isWorkerPolicyListSorted = true;
        }
        return workerPolicyList;
    }

    public void setWorkerPolicyList(List<ProcessTaskStepWorkerPolicyVo> workerPolicyList) {
        this.workerPolicyList = workerPolicyList;
    }

    public JSONObject getParamObj() {
        return paramObj;
    }

    public Boolean getIsAllDone() {
        return isAllDone;
    }

    public void setIsAllDone(Boolean isAllDone) {
        if (isAllDone) {
            this.isCurrentUserDone = isAllDone;
        }
        this.isAllDone = isAllDone;
    }

    public Boolean getIsCurrentUserDone() {
        return isCurrentUserDone;
    }

    public void setIsCurrentUserDone(Boolean isCurrentUserDone) {
        this.isCurrentUserDone = isCurrentUserDone;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Long getExpireTimeLong() {
        return expireTimeLong;
    }

    public void setExpireTimeLong(Long expireTimeLong) {
        this.expireTimeLong = expireTimeLong;
    }

//    public String getFormUuid() {
//        return formUuid;
//    }
//
//    public void setFormUuid(String formUuid) {
//        this.formUuid = formUuid;
//    }

//    public List<ProcessTaskStepFormAttributeVo> getFormAttributeList() {
//        return formAttributeList;
//    }
//
//    public void setFormAttributeList(List<ProcessTaskStepFormAttributeVo> formAttributeList) {
//        this.formAttributeList = formAttributeList;
//    }

    public Long getStartProcessTaskStepId() {
        if (startProcessTaskStepId == null) {
            return id;
        }
        return startProcessTaskStepId;
    }

    public void setStartProcessTaskStepId(Long startProcessTaskStepId) {
        this.startProcessTaskStepId = startProcessTaskStepId;
    }

    public String getConfigHash() {
        return configHash;
    }

    public void setConfigHash(String configHash) {
        this.configHash = configHash;
    }

    public List<ProcessTaskStepUserVo> getMinorUserList() {
        return minorUserList;
    }

    public void setMinorUserList(List<ProcessTaskStepUserVo> minorUserList) {
        this.minorUserList = minorUserList;
    }

    public ProcessTaskStepReplyVo getComment() {
        return comment;
    }

    public void setComment(ProcessTaskStepReplyVo comment) {
        this.comment = comment;
    }

    public List<ProcessTaskStepReplyVo> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<ProcessTaskStepReplyVo> commentList) {
        this.commentList = commentList;
    }

    public List<ProcessTaskActionVo> getActionList() {
        return actionList;
    }

    public void setActionList(List<ProcessTaskActionVo> actionList) {
        this.actionList = actionList;
    }

    public Integer getIsView() {
        return isView;
    }

    public void setIsView(Integer isView) {
        this.isView = isView;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public List<AssignableWorkerStepVo> getAssignableWorkerStepList() {
        return assignableWorkerStepList;
    }

    public void setAssignableWorkerStepList(List<AssignableWorkerStepVo> assignableWorkerStepList) {
        this.assignableWorkerStepList = assignableWorkerStepList;
    }

    public ProcessTaskStepUserVo getMajorUser() {
        return majorUser;
    }

    public void setMajorUser(ProcessTaskStepUserVo majorUser) {
        this.majorUser = majorUser;
    }

    public List<ProcessTaskSlaTimeVo> getSlaTimeList() {
        return slaTimeList;
    }

    public void setSlaTimeList(List<ProcessTaskSlaTimeVo> slaTimeList) {
        this.slaTimeList = slaTimeList;
    }

    public Boolean getIsAutoGenerateId() {
        return isAutoGenerateId;
    }

    public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
        this.isAutoGenerateId = isAutoGenerateId;
    }

    public JSONObject getProcessTaskStepData() {
        return processTaskStepData;
    }

    public void setProcessTaskStepData(JSONObject processTaskStepData) {
        this.processTaskStepData = processTaskStepData;
    }

    public Object getHandlerStepInfo() {
        return handlerStepInfo;
    }

    public void setHandlerStepInfo(Object handlerStepInfo) {
        this.handlerStepInfo = handlerStepInfo;
    }

    public List<ProcessTaskStepVo> getForwardNextStepList() {
        return forwardNextStepList;
    }

    public void setForwardNextStepList(List<ProcessTaskStepVo> forwardNextStepList) {
        this.forwardNextStepList = forwardNextStepList;
    }

    public List<ProcessTaskStepVo> getBackwardNextStepList() {
        return backwardNextStepList;
    }

    public void setBackwardNextStepList(List<ProcessTaskStepVo> backwardNextStepList) {
        this.backwardNextStepList = backwardNextStepList;
    }

    public List<ProcessTaskStepRemindVo> getProcessTaskStepRemindList() {
        return processTaskStepRemindList;
    }

    public void setProcessTaskStepRemindList(List<ProcessTaskStepRemindVo> processTaskStepRemindList) {
        this.processTaskStepRemindList = processTaskStepRemindList;
    }

    public List<FormAttributeVo> getFormAttributeVoList() {
        return formAttributeVoList;
    }

    public void setFormAttributeVoList(List<FormAttributeVo> formAttributeVoList) {
        this.formAttributeVoList = formAttributeVoList;
    }

    public String getOriginalUser() {
        return originalUser;
    }

    public void setOriginalUser(String originalUser) {
        this.originalUser = originalUser;
    }

    public UserVo getOriginalUserVo() {
        return originalUserVo;
    }

    public void setOriginalUserVo(UserVo originalUserVo) {
        this.originalUserVo = originalUserVo;
    }

    public ProcessTaskStepAgentVo getProcessTaskStepAgentVo() {
        return processTaskStepAgentVo;
    }

    public void setProcessTaskStepAgentVo(ProcessTaskStepAgentVo processTaskStepAgentVo) {
        this.processTaskStepAgentVo = processTaskStepAgentVo;
    }

    public int getUpdateActiveTime() {
        return updateActiveTime;
    }

    public void setUpdateActiveTime(int updateActiveTime) {
        this.updateActiveTime = updateActiveTime;
    }

    public int getUpdateStartTime() {
        return updateStartTime;
    }

    public void setUpdateStartTime(int updateStartTime) {
        this.updateStartTime = updateStartTime;
    }

    public int getUpdateEndTime() {
        return updateEndTime;
    }

    public void setUpdateEndTime(int updateEndTime) {
        this.updateEndTime = updateEndTime;
    }

    public ProcessCommentTemplateVo getCommentTemplate() {
        return commentTemplate;
    }

    public void setCommentTemplate(ProcessCommentTemplateVo commentTemplate) {
        this.commentTemplate = commentTemplate;
    }
    @Deprecated
    public JSONObject getProcessTaskStepTask() {
        return processTaskStepTask;
    }
    @Deprecated
    public void setProcessTaskStepTask(JSONObject processTaskStepTask) {
        this.processTaskStepTask = processTaskStepTask;
    }

//    public List<ProcessTaskStepTaskVo> getProcessTaskStepTaskVoList() {
//        return processTaskStepTaskVoList;
//    }
//
//    public void setProcessTaskStepTaskVoList(List<ProcessTaskStepTaskVo> processTaskStepTaskVoList) {
//        this.processTaskStepTaskVoList = processTaskStepTaskVoList;
//    }

    public List<TaskConfigVo> getTaskConfigList() {
        return taskConfigList;
    }

    public void setTaskConfigList(List<TaskConfigVo> taskConfigList) {
        this.taskConfigList = taskConfigList;
    }

    public ProcessTaskStepTaskVo getProcessTaskStepTaskVo() {
        return processTaskStepTaskVo;
    }

    public void setProcessTaskStepTaskVo(ProcessTaskStepTaskVo processTaskStepTaskVo) {
        this.processTaskStepTaskVo = processTaskStepTaskVo;
    }

    public List<Long> getParallelActivateStepIdList() {
        return parallelActivateStepIdList;
    }

    public void setParallelActivateStepIdList(List<Long> parallelActivateStepIdList) {
        this.parallelActivateStepIdList = parallelActivateStepIdList;
    }

    public List<ProcessTagVo> getProcessTagList() {
        return processTagList;
    }

    public void setProcessTagList(List<ProcessTagVo> processTagList) {
        this.processTagList = processTagList;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public void setNextStepName(String nextStepName) {
        this.nextStepName = nextStepName;
    }

    public Long getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(Long nextStepId) {
        this.nextStepId = nextStepId;
    }

    public String getTaskConfigHash() {
        return taskConfigHash;
    }

    public void setTaskConfigHash(String taskConfigHash) {
        this.taskConfigHash = taskConfigHash;
    }

    public Long getSubProcessTaskId() {
        return subProcessTaskId;
    }

    public void setSubProcessTaskId(Long subProcessTaskId) {
        this.subProcessTaskId = subProcessTaskId;
    }

    public List<String> getViewPrevNodeUuidList() {
        return viewPrevNodeUuidList;
    }

    public void setViewPrevNodeUuidList(List<String> viewPrevNodeUuidList) {
        this.viewPrevNodeUuidList = viewPrevNodeUuidList;
    }

    public List<ProcessTaskStepVo> getViewPrevNodeList() {
        return viewPrevNodeList;
    }

    public void setViewPrevNodeList(List<ProcessTaskStepVo> viewPrevNodeList) {
        this.viewPrevNodeList = viewPrevNodeList;
    }

    public Integer getIsInTheCurrentStepTab() {
        return isInTheCurrentStepTab;
    }

    public void setIsInTheCurrentStepTab(Integer isInTheCurrentStepTab) {
        this.isInTheCurrentStepTab = isInTheCurrentStepTab;
    }

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }
}
