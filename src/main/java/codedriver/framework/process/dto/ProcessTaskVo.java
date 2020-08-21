package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.dto.TeamVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;

public class ProcessTaskVo {
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "父工单id", type = ApiParamType.LONG)
	private Long parentId;
	@EntityField(name = "标题", type = ApiParamType.STRING)
	private String title;
	@EntityField(name = "流程uuid", type = ApiParamType.STRING)
	private String processUuid;
	@EntityField(name = "服务uuid", type = ApiParamType.STRING)
	private String channelUuid;
	@EntityField(name = "服务路径", type = ApiParamType.STRING)
	private String channelPath;
	@EntityField(name = "优先级uuid", type = ApiParamType.STRING)
	private String priorityUuid;
	@EntityField(name = "优先级名", type = ApiParamType.STRING)
	private String priorityName;
	@EntityField(name = "工单流程图信息", type = ApiParamType.STRING)
	private String config;
	private String configPath;
	private JSONObject configObj;
	@EntityField(name = "状态", type = ApiParamType.STRING)
	private String status;
	@EntityField(name = "状态信息", type = ApiParamType.JSONOBJECT)
	private ProcessTaskStatusVo statusVo;
	@EntityField(name = "上报人userUuid", type = ApiParamType.STRING)
	private String owner;
	@EntityField(name = "上报人", type = ApiParamType.STRING)
	private String ownerName;
	@EntityField(name = "代报人userUuid", type = ApiParamType.STRING)
	private String reporter;
	@EntityField(name = "代报人", type = ApiParamType.STRING)
	private String reporterName;
	@EntityField(name = "开始时间", type = ApiParamType.LONG)
	private Date startTime;
	@EntityField(name = "结束时间", type = ApiParamType.LONG)
	private Date endTime;
	@EntityField(name = "耗时(秒)", type = ApiParamType.LONG)
	private Long timeCost;
	private String timeCostStr;
	@EntityField(name = "超时时间点", type = ApiParamType.LONG)
	private Date expireTime;
	private String configHash;

	private List<ProcessTaskStepVo> stepList;
	
	@EntityField(name = "优先级信息", type = ApiParamType.JSONOBJECT)
	private PriorityVo priority;
	@EntityField(name = "工单表单信息", type = ApiParamType.STRING)
	private String formConfig;
	@EntityField(name = "是否存在旧工单表单信息", type = ApiParamType.STRING)
    private int isHasOldFormProp = 0;
	@EntityField(name = "工单表单属性值", type = ApiParamType.JSONOBJECT)
	Map<String, Object> formAttributeDataMap;
	@EntityField(name = "工作时间窗口uuid", type = ApiParamType.STRING)
	private String worktimeUuid;

	@EntityField(name = "服务类型信息", type = ApiParamType.JSONOBJECT)
	private ChannelTypeVo channelType;

	@EntityField(name = "工单开始步骤信息", type = ApiParamType.JSONOBJECT)
	ProcessTaskStepVo startProcessTaskStep;
	@EntityField(name = "工单当前步骤信息", type = ApiParamType.JSONOBJECT)
	ProcessTaskStepVo currentProcessTaskStep;
	
	@EntityField(name = "上报人公司列表", type = ApiParamType.JSONARRAY)
	private transient List<TeamVo> ownerCompanyList = new ArrayList<>();
	
	private transient Boolean isAutoGenerateId = true;
	
	public ProcessTaskVo() {

	}

	public ProcessTaskVo(Long _id, String _status) {
		this.id = _id;
		this.status = _status;
	}

	public ProcessTaskVo(Long _id) {
		this.id = _id;
	}

	public synchronized Long getId() {
		if(id == null && isAutoGenerateId) {
			id = SnowflakeUtil.uniqueLong();
		}
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public JSONObject getConfigObj() {
		return configObj;
	}

	public void setConfigObj(JSONObject configObj) {
		this.configObj = configObj;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
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
		if(statusVo == null && StringUtils.isNotBlank(status)) {
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

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
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

	public String getFormConfig() {
		return formConfig;
	}

	public void setFormConfig(String formConfig) {
		this.formConfig = formConfig;
	}

	public Map<String, Object> getFormAttributeDataMap() {
		return formAttributeDataMap;
	}

	public void setFormAttributeDataMap(Map<String, Object> formAttributeDataMap) {
		this.formAttributeDataMap = formAttributeDataMap;
	}

	public String getWorktimeUuid() {
		return worktimeUuid;
	}

	public void setWorktimeUuid(String worktimeUuid) {
		this.worktimeUuid = worktimeUuid;
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

    public int getIsHasOldFormProp() {
        return isHasOldFormProp;
    }

    public void setIsHasOldFormProp(int isHasOldFormProp) {
        this.isHasOldFormProp = isHasOldFormProp;
    }

}
