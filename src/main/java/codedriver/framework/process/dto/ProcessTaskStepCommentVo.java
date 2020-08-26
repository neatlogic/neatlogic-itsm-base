package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.List;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.file.dto.FileVo;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessTaskStepCommentVo extends BaseEditorVo {
	@EntityField(name = "回复id", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "工单id", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "步骤id", type = ApiParamType.LONG)
	private Long processTaskStepId;
	@EntityField(name = "描述内容", type = ApiParamType.STRING)
	private String content;
	@EntityField(name = "类型", type = ApiParamType.STRING)
    private String type;
	@EntityField(name = "附件列表", type = ApiParamType.JSONARRAY)
	private List<FileVo> fileList = new ArrayList<>();
	@EntityField(name = "附件id列表", type = ApiParamType.JSONARRAY)
    private List<Long> fileIdList;
	
	private transient String contentHash;
	private transient String fileIdListHash;
	
	public ProcessTaskStepCommentVo() {}
	public ProcessTaskStepCommentVo(ProcessTaskStepContentVo processTaskStepContentVo) {
	    super.setFcd(processTaskStepContentVo.getFcd());
        super.setLcd(processTaskStepContentVo.getLcd());
	    super.setFcu(processTaskStepContentVo.getFcu());
	    super.setLcu(processTaskStepContentVo.getLcu());
	    super.setFcuName(processTaskStepContentVo.getFcuName());
	    super.setLcuName(processTaskStepContentVo.getLcuName());
	    this.id = processTaskStepContentVo.getId();
	    this.processTaskId = processTaskStepContentVo.getProcessTaskId();
	    this.processTaskStepId = processTaskStepContentVo.getProcessTaskStepId();
	    this.type = processTaskStepContentVo.getType();
	    this.contentHash = processTaskStepContentVo.getContentHash();
	}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
	public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setFileList(List<FileVo> fileList) {
		this.fileList = fileList;
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
    public String getFileIdListHash() {
        return fileIdListHash;
    }
    public void setFileIdListHash(String fileIdListHash) {
        this.fileIdListHash = fileIdListHash;
    }

}
