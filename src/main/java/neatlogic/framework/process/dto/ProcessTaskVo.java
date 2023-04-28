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
    @EntityField(name = "工单id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "父工单id", type = ApiParamType.LONG)
    private Long parentId;
    @EntityField(name = "工单号", type = ApiParamType.STRING)
    private String serialNumber;
    @EntityField(name = "标题", type = ApiParamType.STRING)
    private String title;
    @EntityField(name = "流程uuid", type = ApiParamType.STRING)
    private String processUuid;
    @EntityField(name = "服务所在目录名称", type = ApiParamType.STRING)
    private String catalogName;
    @EntityField(name = "服务")
    private ChannelVo channelVo;
    @EntityField(name = "服务uuid", type = ApiParamType.STRING)
    private String channelUuid;
    @EntityField(name = "服务路径", type = ApiParamType.STRING)
    private String channelPath;
    @EntityField(name = "优先级uuid", type = ApiParamType.STRING)
    private String priorityUuid;
    //    @EntityField(name = "工单流程图信息", type = ApiParamType.STRING)
    @JSONField(serialize = false)
    private String config;
    //    private String configPath;
//    private JSONObject configObj;
    @EntityField(name = "状态", type = ApiParamType.STRING)
    private String status;
    @EntityField(name = "状态信息", type = ApiParamType.JSONOBJECT)
    private ProcessTaskStatusVo statusVo;
    @EntityField(name = "上报人userUuid", type = ApiParamType.STRING)
    private String owner;
    @EntityField(name = "上报人")
    private UserVo ownerVo;
    //    @EntityField(name = "上报人", type = ApiParamType.STRING)
//    private String ownerName;
//    @EntityField(name = "上报人等级", type = ApiParamType.INTEGER)
//    private Integer ownerVipLevel;
    @EntityField(name = "代报人userUuid", type = ApiParamType.STRING)
    private String reporter;
    @EntityField(name = "代报人")
    private UserVo reporterVo;
    //    @EntityField(name = "代报人", type = ApiParamType.STRING)
//    private String reporterName;
    @EntityField(name = "开始时间", type = ApiParamType.LONG)
    private Date startTime;
    @EntityField(name = "结束时间", type = ApiParamType.LONG)
    private Date endTime;
    @EntityField(name = "耗时", type = ApiParamType.LONG)
    private Long timeCost;
    private String timeCostStr;
    @EntityField(name = "超时时间点", type = ApiParamType.LONG)
    private Date expireTime;
    private String configHash;

    private List<ProcessTaskStepVo> stepList = new ArrayList<>();
    @EntityField(name = "是否显示优先级", type = ApiParamType.INTEGER)
    private Integer isNeedPriority = 1;
    @EntityField(name = "优先级信息", type = ApiParamType.JSONOBJECT)
    private PriorityVo priority;
    @EntityField(name = "工单表单信息", type = ApiParamType.JSONOBJECT)
    private JSONObject formConfig;
    @EntityField(name = "工单表单信息授权列表", type = ApiParamType.JSONARRAY)
    private JSONArray formConfigAuthorityList;
    @EntityField(name = "表单配置授权隐藏属性uuid列表")
    private List<String> formAttributeHideList;
    @EntityField(name = "是否存在旧工单表单信息", type = ApiParamType.STRING)
    private int isHasOldFormProp = 0;
    @EntityField(name = "工单表单属性值", type = ApiParamType.JSONOBJECT)
    private Map<String, Object> formAttributeDataMap = new HashMap<>();
    private List<ProcessTaskFormAttributeDataVo> processTaskFormAttributeDataList;
    @EntityField(name = "工作时间窗口uuid", type = ApiParamType.STRING)
    private String worktimeUuid;

    @EntityField(name = "服务类型uuid", type = ApiParamType.STRING)
    private String channelTypeUuid;
    @EntityField(name = "服务类型信息", type = ApiParamType.JSONOBJECT)
    private ChannelTypeVo channelType;

    @EntityField(name = "工单开始步骤信息", type = ApiParamType.JSONOBJECT)
    ProcessTaskStepVo startProcessTaskStep;
    @EntityField(name = "工单当前步骤信息", type = ApiParamType.JSONOBJECT)
    ProcessTaskStepVo currentProcessTaskStep;

    @EntityField(name = "上报人公司列表", type = ApiParamType.JSONARRAY)
    @JSONField(serialize = false)
    private List<TeamVo> ownerCompanyList = new ArrayList<>();

    @EntityField(name = "上报人部门列表", type = ApiParamType.JSONARRAY)
    @JSONField(serialize = false)
    private List<TeamVo> ownerDepartmentList = new ArrayList<>();

    @EntityField(name = "评分信息", type = ApiParamType.STRING)
    private String scoreInfo;

    @EntityField(name = "转报工单信息列表", type = ApiParamType.JSONOBJECT)
    private List<ProcessTaskVo> tranferReportProcessTaskList = new ArrayList<>();
    @EntityField(name = "转报/关联方向", type = ApiParamType.STRING)
    private String tranferReportDirection;
    @EntityField(name = "转报/关联类型", type = ApiParamType.STRING)
    private String channelTypeRelationName;
    @JSONField(serialize = false)
    private Boolean isAutoGenerateId = true;
    @EntityField(name = "重做步骤列表", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepVo> redoStepList = new ArrayList<>();
    @EntityField(name = "评分模板", type = ApiParamType.JSONARRAY)
    private ScoreTemplateVo scoreTemplateVo;
    @JSONField(serialize = false)
    private JSONObject paramObj;
    @EntityField(name = "是否显示，1：显示，0：隐藏", type = ApiParamType.INTEGER)
    private Integer isShow;
    @EntityField(name = "是否已关注，1：是，0：否", type = ApiParamType.INTEGER)
    private Integer isFocus = 0;
    @EntityField(name = "评论附件列表", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskStepReplyVo> commentList = new ArrayList<>();
    @EntityField(name = "标签列表", type = ApiParamType.JSONARRAY)
    private List<ProcessTagVo> tagVoList;
    @EntityField(name = "标签列表", type = ApiParamType.JSONARRAY)
    private List<String> tagList;
    @EntityField(name = "移动端表单交互类型，1：下探页面；0：当前页", type = ApiParamType.INTEGER)
    private Integer mobileFormUIType;
    @JSONField(serialize = false)
    private List<ProcessTaskStepRelVo> stepRelList;

    @JSONField(serialize = false)
    @EntityField(name = "上报人userName", type = ApiParamType.STRING)
    private String ownerName;
    @JSONField(serialize = false)
    @EntityField(name = "代报人userName", type = ApiParamType.STRING)
    private String reporterName;
    @JSONField(serialize = false)
    @EntityField(name = "优先级名称", type = ApiParamType.STRING)
    private String priorityName;
    @JSONField(serialize = false)
    @EntityField(name = "工作时间窗口名称", type = ApiParamType.STRING)
    private String worktimeName;
    @JSONField(serialize = false)
    @EntityField(name = "服务类型名称", type = ApiParamType.STRING)
    private String channelTypeName;
    @JSONField(serialize = false)
    @EntityField(name = "服务名称", type = ApiParamType.STRING)
    private String channelName;

    @EntityField(name = "关注人列表", type = ApiParamType.JSONARRAY)
    private List<UserVo> focusUserList;
    @EntityField(name = "关注人uuid列表", type = ApiParamType.JSONARRAY)
    private List<String> focusUserUuidList;

    @EntityField(name = "sla列表", type = ApiParamType.JSONARRAY)
    private List<ProcessTaskSlaVo> processTaskSlaVoList;

    @EntityField(name = "是否有权限修改工单关注人", type = ApiParamType.INTEGER)
    private Integer canEditFocusUser;

    @EntityField(name = "工单来源", type = ApiParamType.STRING)
    private String source = ProcessTaskSource.PC.getValue();
    @EntityField(name = "工单来源中文名", type = ApiParamType.STRING)
    private String sourceName;

    @EntityField(name = "是否已删除，1：是，0：否", type = ApiParamType.INTEGER)
    private Integer isDeleted = 0;

    @EntityField(name = "是否默认展开基本信息", type = ApiParamType.INTEGER)
    private Integer isShowBaseInfo = 0;

    @JSONField(serialize = false)
    @EntityField(name = "工单中心工单排序下标", type = ApiParamType.INTEGER)
    private Integer index;

    @EntityField(name = "是否需要评分，1：是，0：否", type = ApiParamType.INTEGER)
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

    public Integer getNeedScore() {
        return needScore;
    }

    public void setNeedScore(Integer needScore) {
        this.needScore = needScore;
    }
}
