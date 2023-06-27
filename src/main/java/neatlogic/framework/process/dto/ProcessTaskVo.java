package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.dto.TeamVo;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.process.constvalue.ProcessTaskSourceFactory;
import neatlogic.framework.process.constvalue.ProcessTaskSource;
import neatlogic.framework.process.constvalue.ProcessTaskStatus;
import neatlogic.framework.process.dto.score.ScoreTemplateVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ProcessTaskVo extends BasePageVo {
    //    @ESKey(type = ESKeyType.PKEY, name = "processTaskId")
    @EntityField(name = "common.id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "common.parentid", type = ApiParamType.LONG)
    private Long parentId;
    @EntityField(name = "term.itsm.serialnumber", type = ApiParamType.STRING)
    private String serialNumber;
    @EntityField(name = "common.title", type = ApiParamType.STRING)
    private String title;
    @EntityField(name = "term.itsm.processuuid", type = ApiParamType.STRING)
    private String processUuid;
    @EntityField(name = "term.itsm.catalogname", type = ApiParamType.STRING)
    private String catalogName;
    @EntityField(name = "term.itsm.channelinfo")
    private ChannelVo channelVo;
    @EntityField(name = "term.itsm.channeluuid", type = ApiParamType.STRING)
    private String channelUuid;
    @EntityField(name = "term.itsm.channelpath", type = ApiParamType.STRING)
    private String channelPath;
    @EntityField(name = "common.priorityuuid", type = ApiParamType.STRING)
    private String priorityUuid;
    //    @EntityField(name = "工单流程图信息", type = ApiParamType.STRING)
    @JSONField(serialize = false)
    private String config;
    //    private String configPath;
//    private JSONObject configObj;
    @EntityField(name = "common.status", type = ApiParamType.STRING)
    private String status;
    @EntityField(name = "common.statusinfo", type = ApiParamType.JSONOBJECT)
    private ProcessTaskStatusVo statusVo;
    @EntityField(name = "term.itsm.owneruuid", type = ApiParamType.STRING)
    private String owner;
    @EntityField(name = "term.itsm.ownerinfo")
    private UserVo ownerVo;
    //    @EntityField(name = "上报人", type = ApiParamType.STRING)
//    private String ownerName;
//    @EntityField(name = "上报人等级", type = ApiParamType.INTEGER)
//    private Integer ownerVipLevel;
    @EntityField(name = "term.itsm.submitteruuid", type = ApiParamType.STRING)
    private String reporter;
    @EntityField(name = "term.itsm.submitterinfo")
    private UserVo reporterVo;
    //    @EntityField(name = "代报人", type = ApiParamType.STRING)
//    private String reporterName;
    @EntityField(name = "common.starttime", type = ApiParamType.LONG)
    private Date startTime;
    @EntityField(name = "common.endtime", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "common.timecost", type = ApiParamType.LONG)
    private Long timeCost;
    private String timeCostStr;
    @EntityField(name = "common.timeoutpoint", type = ApiParamType.LONG)
    private Date expireTime;
    private String configHash;

    private List<ProcessTaskStepVo> stepList = new ArrayList<>();
    @EntityField(name = "common.isneedpriority", type = ApiParamType.INTEGER)
    private Integer isNeedPriority = 1;
    @EntityField(name = "common.priority", type = ApiParamType.JSONOBJECT)
    private PriorityVo priority;
    @EntityField(name = "term.itsm.processtaskformconfig", type = ApiParamType.JSONOBJECT)
    private JSONObject formConfig;
    @EntityField(name = "term.itsm.formconfigauthoritylist", type = ApiParamType.JSONARRAY)
    private JSONArray formConfigAuthorityList;
    @EntityField(name = "term.itsm.formattributehidelist")
    private List<String> formAttributeHideList;
    @EntityField(name = "term.itsm.ishasoldformprop", type = ApiParamType.STRING)
    private int isHasOldFormProp = 0;
    @EntityField(name = "term.itsm.processtaskformattributedata", type = ApiParamType.JSONOBJECT)
    private Map<String, Object> formAttributeDataMap = new HashMap<>();
    private List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList;
    @EntityField(name = "common.worktimeuuid", type = ApiParamType.STRING)
    private String worktimeUuid;

    @EntityField(name = "term.itsm.channeltypeuuid", type = ApiParamType.STRING)
    private String channelTypeUuid;
    @EntityField(name = "term.itsm.channeltypeinfo", type = ApiParamType.JSONOBJECT)
    private ChannelTypeVo channelType;

    @EntityField(name = "term.itsm.startprocesstaskstep", type = ApiParamType.JSONOBJECT)
    ProcessTaskStepVo startProcessTaskStep;
    @EntityField(name = "term.itsm.currentprocesstaskstep", type = ApiParamType.JSONOBJECT)
    ProcessTaskStepVo currentProcessTaskStep;

    @EntityField(name = "term.itsm.ownercompanylist", type = ApiParamType.JSONARRAY)
    @JSONField(serialize = false)
    private List<TeamVo> ownerCompanyList = new ArrayList<>();

    @EntityField(name = "term.itsm.ownerdepartmentlist", type = ApiParamType.JSONARRAY)
    @JSONField(serialize = false)
    private List<TeamVo> ownerDepartmentList = new ArrayList<>();

    @EntityField(name = "common.scoreinfo", type = ApiParamType.STRING)
    private String scoreInfo;

    @EntityField(name = "term.itsm.tranferreportprocesstasklist", type = ApiParamType.JSONOBJECT)
    private List<ProcessTaskVo> tranferReportProcessTaskList = new ArrayList<>();
    @EntityField(name = "term.itsm.tranferreportdirection", type = ApiParamType.STRING)
    private String tranferReportDirection;
    @EntityField(name = "term.itsm.channeltyperelationname", type = ApiParamType.STRING)
    private String channelTypeRelationName;
    @JSONField(serialize = false)
    private Boolean isAutoGenerateId = true;
    @EntityField(name = "term.itsm.redosteplist", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepVo> redoStepList = new ArrayList<>();
    @EntityField(name = "term.itsm.scoretemplate", type = ApiParamType.JSONARRAY)
    private ScoreTemplateVo scoreTemplateVo;
    @JSONField(serialize = false)
    private JSONObject paramObj;
    @EntityField(name = "common.isshow", type = ApiParamType.INTEGER)
    private Integer isShow;
    @EntityField(name = "common.isfocus", type = ApiParamType.INTEGER)
    private Integer isFocus = 0;
    @EntityField(name = "term.itsm.commentlist", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepReplyVo> commentList = new ArrayList<>();
    @EntityField(name = "common.taglist", type = ApiParamType.JSONARRAY)
    private List<ProcessTagVo> tagVoList;
    @EntityField(name = "common.taglist", type = ApiParamType.JSONARRAY)
    private List<String> tagList;
    @EntityField(name = "term.itsm.mobileformuitype", type = ApiParamType.INTEGER)
    private Integer mobileFormUIType;
    @JSONField(serialize = false)
    private List<ProcessTaskStepRelVo> stepRelList;

    @JSONField(serialize = false)
    @EntityField(name = "term.itsm.ownername", type = ApiParamType.STRING)
    private String ownerName;
    @JSONField(serialize = false)
    @EntityField(name = "term.itsm.submittername", type = ApiParamType.STRING)
    private String reporterName;
    @JSONField(serialize = false)
    @EntityField(name = "common.priorityname", type = ApiParamType.STRING)
    private String priorityName;
    @JSONField(serialize = false)
    @EntityField(name = "common.worktimename", type = ApiParamType.STRING)
    private String worktimeName;
    @JSONField(serialize = false)
    @EntityField(name = "term.itsm.channeltypename", type = ApiParamType.STRING)
    private String channelTypeName;
    @JSONField(serialize = false)
    @EntityField(name = "term.itsm.channelname", type = ApiParamType.STRING)
    private String channelName;

    @EntityField(name = "common.focususerlist", type = ApiParamType.JSONARRAY)
    private List<UserVo> focusUserList;
    @EntityField(name = "common.focususeruuidlist", type = ApiParamType.JSONARRAY)
    private List<String> focusUserUuidList;

    @EntityField(name = "term.itsm.processtaskslalist", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskSlaVo> processTaskSlaVoList;

    @EntityField(name = "term.itsm.caneditfocususer", type = ApiParamType.INTEGER)
    private Integer canEditFocusUser;

    @EntityField(name = "common.source", type = ApiParamType.STRING)
    private String source = ProcessTaskSource.PC.getValue();
    @EntityField(name = "common.sourcename", type = ApiParamType.STRING)
    private String sourceName;

    @EntityField(name = "common.isdeleted", type = ApiParamType.INTEGER)
    private Integer isDeleted = 0;

    @EntityField(name = "term.itsm.isshowbaseinfo", type = ApiParamType.INTEGER)
    private Integer isShowBaseInfo = 0;

    @EntityField(name = "term.itsm.isshowprocesstaskstepcommenteditortoolbar", type = ApiParamType.INTEGER)
    private Integer isShowProcessTaskStepCommentEditorToolbar = 0;

    @JSONField(serialize = false)
    @EntityField(name = "term.itsm.workcenterprocesstasksortindex", type = ApiParamType.INTEGER)
    private Integer index;

    @EntityField(name = "common.needscore", type = ApiParamType.INTEGER)
    private Integer needScore;

    public ProcessTaskVo() {

    }

    public ProcessTaskVo(Long _id, ProcessTaskStatus _status) {
        this.id = _id;
        this.status = _status.getValue();
    }

    public ProcessTaskVo(Long _id) {
        this.id = _id;
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

//    public String getConfig() {
//        return config;
//    }
//
//    public void setConfig(String config) {
//        this.config = config;
//    }
//
//    public String getConfigPath() {
//        return configPath;
//    }
//
//    public void setConfigPath(String configPath) {
//        this.configPath = configPath;
//    }
//
//    public JSONObject getConfigObj() {
//        return configObj;
//    }
//
//    public void setConfigObj(JSONObject configObj) {
//        this.configObj = configObj;
//    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public UserVo getOwnerVo() {
        return ownerVo;
    }

    public void setOwnerVo(UserVo ownerVo) {
        this.ownerVo = ownerVo;
    }

    //    public String getOwnerName() {
//        return ownerName;
//    }
//
//    public void setOwnerName(String ownerName) {
//        this.ownerName = ownerName;
//    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public UserVo getReporterVo() {
        return reporterVo;
    }

    public void setReporterVo(UserVo reporterVo) {
        this.reporterVo = reporterVo;
    }

    //    public String getReporterName() {
//        return reporterName;
//    }
//
//    public void setReporterName(String reporterName) {
//        this.reporterName = reporterName;
//    }

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

    public Long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Long timeCost) {
        this.timeCost = timeCost;
    }

    public String getTimeCostStr() {
        return timeCostStr;
    }

    public void setTimeCostStr(String timeCostStr) {
        this.timeCostStr = timeCostStr;
    }

    public List<ProcessTaskStepVo> getStepList() {
        return stepList;
    }

    public void setStepList(List<ProcessTaskStepVo> stepList) {
        this.stepList = stepList;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcessUuid() {
        return processUuid;
    }

    public void setProcessUuid(String processUuid) {
        this.processUuid = processUuid;
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

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getChannelUuid() {
        return channelUuid;
    }

    public void setChannelUuid(String channelUuid) {
        this.channelUuid = channelUuid;
    }

    public String getChannelPath() {
        return channelPath;
    }

    public void setChannelPath(String channelPath) {
        this.channelPath = channelPath;
    }

    public String getConfigHash() {
        return configHash;
    }

    public void setConfigHash(String configHash) {
        this.configHash = configHash;
    }

    public String getPriorityUuid() {
        return priorityUuid;
    }

    public void setPriorityUuid(String priorityUuid) {
        this.priorityUuid = priorityUuid;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public PriorityVo getPriority() {
        return priority;
    }

    public void setPriority(PriorityVo priority) {
        this.priority = priority;
    }

    public JSONObject getFormConfig() {
        return formConfig;
    }

    public void setFormConfig(JSONObject formConfig) {
        this.formConfig = formConfig;
    }

    public JSONArray getFormConfigAuthorityList() {
        return formConfigAuthorityList;
    }

    public void setFormConfigAuthorityList(JSONArray formConfigAuthorityList) {
        this.formConfigAuthorityList = formConfigAuthorityList;
    }

    public List<String> getFormAttributeHideList() {
        return formAttributeHideList;
    }

    public void setFormAttributeHideList(List<String> formAttributeHideList) {
        this.formAttributeHideList = formAttributeHideList;
    }

    public Map<String, Object> getFormAttributeDataMap() {
        return formAttributeDataMap;
    }

    public void setFormAttributeDataMap(Map<String, Object> formAttributeDataMap) {
        this.formAttributeDataMap = formAttributeDataMap;
    }

    public List<ProcessTaskFormAttributeDataVo> getProcessTaskFormAttributeDataList() {
        return processTaskFormAttributeDataList;
    }

    public void setProcessTaskFormAttributeDataList(List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList) {
        this.processTaskFormAttributeDataList = processTaskFormAttributeDataList;
    }

    public String getWorktimeUuid() {
        return worktimeUuid;
    }

    public void setWorktimeUuid(String worktimeUuid) {
        this.worktimeUuid = worktimeUuid;
    }

    public String getChannelTypeUuid() {
        return channelTypeUuid;
    }

    public void setChannelTypeUuid(String channelTypeUuid) {
        this.channelTypeUuid = channelTypeUuid;
    }

    public ChannelTypeVo getChannelType() {
        return channelType;
    }

    public void setChannelType(ChannelTypeVo channelType) {
        this.channelType = channelType;
    }

    public ProcessTaskStepVo getStartProcessTaskStep() {
        return startProcessTaskStep;
    }

    public void setStartProcessTaskStep(ProcessTaskStepVo startProcessTaskStepVo) {
        this.startProcessTaskStep = startProcessTaskStepVo;
    }

    public ProcessTaskStepVo getCurrentProcessTaskStep() {
        return currentProcessTaskStep;
    }

    public void setCurrentProcessTaskStep(ProcessTaskStepVo currentProcessTaskStepVo) {
        this.currentProcessTaskStep = currentProcessTaskStepVo;
    }

    public Boolean getIsAutoGenerateId() {
        return isAutoGenerateId;
    }

    public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
        this.isAutoGenerateId = isAutoGenerateId;
    }

    public List<TeamVo> getOwnerCompanyList() {
        return ownerCompanyList;
    }

    public void setOwnerCompanyList(List<TeamVo> ownerCompanyList) {
        this.ownerCompanyList = ownerCompanyList;
    }

    public List<TeamVo> getOwnerDepartmentList() {
        return ownerDepartmentList;
    }

    public void setOwnerDepartmentList(List<TeamVo> ownerDepartmentList) {
        this.ownerDepartmentList = ownerDepartmentList;
    }

    public int getIsHasOldFormProp() {
        return isHasOldFormProp;
    }

    public void setIsHasOldFormProp(int isHasOldFormProp) {
        this.isHasOldFormProp = isHasOldFormProp;
    }

    public String getScoreInfo() {
        return scoreInfo;
    }

    public void setScoreInfo(String scoreInfo) {
        this.scoreInfo = scoreInfo;
    }

    public List<ProcessTaskVo> getTranferReportProcessTaskList() {
        return tranferReportProcessTaskList;
    }

    public void setTranferReportProcessTaskList(List<ProcessTaskVo> tranferReportProcessTaskList) {
        this.tranferReportProcessTaskList = tranferReportProcessTaskList;
    }

    public String getTranferReportDirection() {
        return tranferReportDirection;
    }

    public void setTranferReportDirection(String tranferReportDirection) {
        this.tranferReportDirection = tranferReportDirection;
    }

    public String getChannelTypeRelationName() {
        return channelTypeRelationName;
    }

    public void setChannelTypeRelationName(String channelTypeRelationName) {
        this.channelTypeRelationName = channelTypeRelationName;
    }

    public List<ProcessTaskStepVo> getRedoStepList() {
        return redoStepList;
    }

    public void setRedoStepList(List<ProcessTaskStepVo> redoStepList) {
        this.redoStepList = redoStepList;
    }

    public ScoreTemplateVo getScoreTemplateVo() {
        return scoreTemplateVo;
    }

    public void setScoreTemplateVo(ScoreTemplateVo scoreTemplateVo) {
        this.scoreTemplateVo = scoreTemplateVo;
    }

    public JSONObject getParamObj() {
        if (paramObj == null) {
            paramObj = new JSONObject();
        }
        return paramObj;
    }

    public void setParamObj(JSONObject paramObj) {
        this.paramObj = paramObj;
    }

//    public Integer getOwnerVipLevel() {
//        return ownerVipLevel;
//    }
//
//    public void setOwnerVipLevel(Integer ownerVipLevel) {
//        this.ownerVipLevel = ownerVipLevel;
//    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(Integer isFocus) {
        this.isFocus = isFocus;
    }

    public List<ProcessTaskStepReplyVo> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<ProcessTaskStepReplyVo> commentList) {
        this.commentList = commentList;
    }

    public List<ProcessTagVo> getTagVoList() {
        return tagVoList;
    }

    public void setTagVoList(List<ProcessTagVo> tagVoList) {
        this.tagVoList = tagVoList;
    }

    public Integer getMobileFormUIType() {
        return mobileFormUIType;
    }

    public void setMobileFormUIType(Integer mobileFormUIType) {
        this.mobileFormUIType = mobileFormUIType;
    }

    public List<ProcessTaskStepRelVo> getStepRelList() {
        return stepRelList;
    }

    public void setStepRelList(List<ProcessTaskStepRelVo> stepRelList) {
        this.stepRelList = stepRelList;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getWorktimeName() {
        return worktimeName;
    }

    public void setWorktimeName(String worktimeName) {
        this.worktimeName = worktimeName;
    }

    public String getChannelTypeName() {
        return channelTypeName;
    }

    public void setChannelTypeName(String channelTypeName) {
        this.channelTypeName = channelTypeName;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public ChannelVo getChannelVo() {
        return channelVo;
    }

    public void setChannelVo(ChannelVo channelVo) {
        this.channelVo = channelVo;
    }

    public List<UserVo> getFocusUserList() {
        return focusUserList;
    }

    public void setFocusUserList(List<UserVo> focusUserList) {
        this.focusUserList = focusUserList;
    }

    public List<ProcessTaskSlaVo> getProcessTaskSlaVoList() {
        return processTaskSlaVoList;
    }

    public void setProcessTaskSlaVoList(List<ProcessTaskSlaVo> processTaskSlaVoList) {
        this.processTaskSlaVoList = processTaskSlaVoList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public List<String> getFocusUserUuidList() {
        return focusUserUuidList;
    }

    public void setFocusUserUuidList(List<String> focusUserUuidList) {
        this.focusUserUuidList = focusUserUuidList;
    }

    public Integer getCanEditFocusUser() {
        return canEditFocusUser;
    }

    public void setCanEditFocusUser(Integer canEditFocusUser) {
        this.canEditFocusUser = canEditFocusUser;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getIsNeedPriority() {
        return isNeedPriority;
    }

    public void setIsNeedPriority(Integer isNeedPriority) {
        this.isNeedPriority = isNeedPriority;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getSourceName() {
        if (StringUtils.isNotBlank(source)) {
            sourceName = ProcessTaskSourceFactory.getSourceName(source);
        }
        return sourceName;
    }

    public Integer getIsShowBaseInfo() {
        return isShowBaseInfo;
    }

    public void setIsShowBaseInfo(Integer isShowBaseInfo) {
        this.isShowBaseInfo = isShowBaseInfo;
    }

    public Integer getIsShowProcessTaskStepCommentEditorToolbar() {
        return isShowProcessTaskStepCommentEditorToolbar;
    }

    public void setIsShowProcessTaskStepCommentEditorToolbar(Integer isShowProcessTaskStepCommentEditorToolbar) {
        this.isShowProcessTaskStepCommentEditorToolbar = isShowProcessTaskStepCommentEditorToolbar;
    }

    public Integer getNeedScore() {
        return needScore;
    }

    public void setNeedScore(Integer needScore) {
        this.needScore = needScore;
    }
}
