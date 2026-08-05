package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.dto.UserVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ProcessTaskImportAuditVo extends BasePageVo {
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.id.name", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.processtaskid.name", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.serialnumber.name", type = ApiParamType.STRING)
	private String serialNumber;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.title.name", type = ApiParamType.STRING)
	private String title;
	@JSONField(serialize = false)
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.channeluuid.name", type = ApiParamType.STRING)
	private String channelUuid;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.status.name", type = ApiParamType.INTEGER)
	private Integer status;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.errorreason.name", type = ApiParamType.STRING)
	private String errorReason;
	@JSONField(serialize = false)
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.owner.name", type = ApiParamType.STRING)
	private String owner;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.importtime.name", type = ApiParamType.LONG)
	private Date importTime;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.ownername.name", type = ApiParamType.STRING)
	private String ownerName;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.ownervo.name")
	private UserVo ownerVo;
	@EntityField(name = "nfpd.processtaskimportauditvo.entityfield.channelname.name", type = ApiParamType.STRING)
	private String channelName;

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

	public Long getProcessTaskId() {
		return processTaskId;
	}

	public void setProcessTaskId(Long processTaskId) {
		this.processTaskId = processTaskId;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public UserVo getOwnerVo() {
		return ownerVo;
	}

	public void setOwnerVo(UserVo ownerVo) {
		this.ownerVo = ownerVo;
	}
}
