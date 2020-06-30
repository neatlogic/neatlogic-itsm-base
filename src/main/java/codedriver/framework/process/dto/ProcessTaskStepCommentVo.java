package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.file.dto.FileVo;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessTaskStepCommentVo {
	@EntityField(name = "回复id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "步骤id", type = ApiParamType.LONG)
	private Long processTaskStepId;
	@EntityField(name = "描述内容", type = ApiParamType.STRING)
	private String content;
	@EntityField(name = "附件列表", type = ApiParamType.JSONARRAY)
	private List<FileVo> fileList = new ArrayList<>();
	@EntityField(name = "创建用户", type = ApiParamType.STRING)
	private String fcu;
	@EntityField(name = "创建用户名称", type = ApiParamType.STRING)
	private String fcuName;
	@EntityField(name = "最后一次修改用户", type = ApiParamType.STRING)
	private String lcu;
	@EntityField(name = "最后一次修改用户名称", type = ApiParamType.STRING)
	private String lcuName;
	@EntityField(name = "创建时间", type = ApiParamType.LONG)
	private Date fcd;
	@EntityField(name = "最后一次修改时间", type = ApiParamType.LONG)
	private Date lcd;
	
	@EntityField(name = "是否可编辑", type = ApiParamType.INTEGER)
	private Integer isEditable;
	@EntityField(name = "是否可删除", type = ApiParamType.INTEGER)
	private Integer isDeletable;

	private transient List<Long> fileIdList;
	private transient String contentHash;
	private transient String fileUuidListHash;
	
	public ProcessTaskStepCommentVo() {
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<FileVo> getFileList() {
		return fileList;
	}
	public void setFileList(List<FileVo> fileList) {
		this.fileList = fileList;
	}
	public Long getId() {
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
	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}
	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}
	public String getFcu() {
		return fcu;
	}
	public void setFcu(String fcu) {
		this.fcu = fcu;
	}
	public String getFcuName() {
		return fcuName;
	}
	public void setFcuName(String fcuName) {
		this.fcuName = fcuName;
	}
	public String getLcu() {
		return lcu;
	}
	public void setLcu(String lcu) {
		this.lcu = lcu;
	}
	public String getLcuName() {
		return lcuName;
	}
	public void setLcuName(String lcuName) {
		this.lcuName = lcuName;
	}
	public Date getFcd() {
		return fcd;
	}
	public void setFcd(Date fcd) {
		this.fcd = fcd;
	}
	public Date getLcd() {
		return lcd;
	}
	public void setLcd(Date lcd) {
		this.lcd = lcd;
	}
	public List<Long> getFileIdList() {
		return fileIdList;
	}
	public void setFileIdList(List<Long> fileIdList) {
		this.fileIdList = fileIdList;
	}
	public String getContentHash() {
		return contentHash;
	}
	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}
	public String getFileUuidListHash() {
		return fileUuidListHash;
	}
	public void setFileUuidListHash(String fileUuidListHash) {
		this.fileUuidListHash = fileUuidListHash;
	}
	public Integer getIsEditable() {
		if(isEditable == null) {
			String currentUser = UserContext.get().getUserUuid();
			if(currentUser != null && currentUser.equals(this.fcu)) {
				isEditable = 1;
			}else {
				isEditable = 0;
			}
		}
		return isEditable;
	}
	public void setIsEditable(Integer isEditable) {
		this.isEditable = isEditable;
	}
	public Integer getIsDeletable() {
		if(isDeletable == null) {
			String currentUser = UserContext.get().getUserUuid();
			if(currentUser != null && currentUser.equals(this.fcu)) {
				isDeletable = 1;
			}else {
				isDeletable = 0;
			}
		}
		return isDeletable;
	}
	public void setIsDeletable(Integer isDeletable) {
		this.isDeletable = isDeletable;
	}

}
