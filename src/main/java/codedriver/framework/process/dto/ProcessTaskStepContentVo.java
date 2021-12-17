package codedriver.framework.process.dto;

import com.alibaba.fastjson.annotation.JSONField;

import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.util.SnowflakeUtil;

public class ProcessTaskStepContentVo extends BaseEditorVo {
	private Long id;
	private Long processTaskId;
	private Long processTaskStepId;
	private String contentHash;
	private String type;
	@JSONField(serialize=false)
    private Boolean isAutoGenerateId = true;

	public ProcessTaskStepContentVo() {

	}

	public ProcessTaskStepContentVo(Long _processTaskId, Long _processTaskStepId, String _contentHash, String _type) {
		this.processTaskId = _processTaskId;
		this.processTaskStepId = _processTaskStepId;
		this.contentHash = _contentHash;
		this.type = _type;
	}
	
	public ProcessTaskStepContentVo(Long _id, String _contentHash) {
        this.id = _id;
        this.contentHash = _contentHash;
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

	public Long getProcessTaskStepId() {
		return processTaskStepId;
	}

	public void setProcessTaskStepId(Long processTaskStepId) {
		this.processTaskStepId = processTaskStepId;
	}

	public String getContentHash() {
		return contentHash;
	}

	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}

	public Long getProcessTaskId() {
		return processTaskId;
	}

	public void setProcessTaskId(Long processTaskId) {
		this.processTaskId = processTaskId;
	}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsAutoGenerateId() {
        return isAutoGenerateId;
    }

    public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
        this.isAutoGenerateId = isAutoGenerateId;
    }

}
