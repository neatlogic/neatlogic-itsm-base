package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;

import java.util.Date;

public class ProcessTaskImportAuditVo {
	@EntityField(name = "导入记录id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "工单ID", type = ApiParamType.LONG)
	private Long processtaskId;
	@EntityField(name = "标题", type = ApiParamType.STRING)
	private String title;
	@EntityField(name = "服务uuid", type = ApiParamType.STRING)
	private String channelUuid;
	@EntityField(name = "状态", type = ApiParamType.INTEGER)
	private Integer status;
	@EntityField(name = "上报失败原因", type = ApiParamType.STRING)
	private String errorReason;
	@EntityField(name = "上报人", type = ApiParamType.STRING)
	private String owner;
	@EntityField(name = "导入时间", type = ApiParamType.LONG)
	private Date importTime;

	public ProcessTaskImportAuditVo() {

	}

	public ProcessTaskImportAuditVo(Long _id) {
		this.id = _id;
	}

	public synchronized Long getId() {
		if(id == null) {
			id = SnowflakeUtil.uniqueLong();
		}
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProcesstaskId() {
		return processtaskId;
	}

	public void setProcesstaskId(Long processtaskId) {
		this.processtaskId = processtaskId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannelUuid() {
		return channelUuid;
	}

	public void setChannelUuid(String channelUuid) {
		this.channelUuid = channelUuid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getImportTime() {
		return importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}
}
