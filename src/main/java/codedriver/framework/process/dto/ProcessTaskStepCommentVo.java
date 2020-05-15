package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.file.dto.FileVo;
import codedriver.framework.process.audithandler.core.IProcessTaskStepAuditDetailHandler;
import codedriver.framework.process.audithandler.core.ProcessTaskStepAuditDetailHandlerFactory;
import codedriver.framework.process.constvalue.ProcessTaskAuditDetailType;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessTaskStepCommentVo {
	@EntityField(name = "回复id", type = ApiParamType.LONG)
	private Long id;
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
	
	public ProcessTaskStepCommentVo() {
	}
	public ProcessTaskStepCommentVo(ProcessTaskStepAuditVo processTaskStepAuditVo) {
		this.id = processTaskStepAuditVo.getId();
		this.fcd = processTaskStepAuditVo.getActionTime();
		this.fcu = processTaskStepAuditVo.getUserId();
		this.fcuName = processTaskStepAuditVo.getUserName();
		List<ProcessTaskStepAuditDetailVo> processTaskStepAuditDetailList = processTaskStepAuditVo.getAuditDetailList();
		for(ProcessTaskStepAuditDetailVo processTaskStepAuditDetailVo : processTaskStepAuditDetailList) {
			IProcessTaskStepAuditDetailHandler auditDetailHandler = ProcessTaskStepAuditDetailHandlerFactory.getHandler(processTaskStepAuditDetailVo.getType());		
			if(ProcessTaskAuditDetailType.CONTENT.getValue().equals(processTaskStepAuditDetailVo.getType())) {
				if(auditDetailHandler != null) {
					auditDetailHandler.handle(processTaskStepAuditDetailVo);
				}
				this.content = processTaskStepAuditDetailVo.getNewContent();
			}else if(ProcessTaskAuditDetailType.FILE.getValue().equals(processTaskStepAuditDetailVo.getType())){
				if(auditDetailHandler != null) {
					auditDetailHandler.handle(processTaskStepAuditDetailVo);
					this.fileList = JSON.parseArray(processTaskStepAuditDetailVo.getNewContent(), FileVo.class);
				}
			}
		}
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

}
