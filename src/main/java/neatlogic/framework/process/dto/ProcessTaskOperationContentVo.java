package neatlogic.framework.process.dto;

import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.process.constvalue.ProcessTaskSourceFactory;
import neatlogic.framework.util.SnowflakeUtil;
import org.apache.commons.lang3.StringUtils;

public class ProcessTaskOperationContentVo extends BaseEditorVo {
	private Long id;
	private Long processTaskId;
	private String contentHash;
	private String type;
	private String source;
	private String sourceName;
	@JSONField(serialize=false)
    private Boolean isAutoGenerateId = true;

	public ProcessTaskOperationContentVo() {

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Boolean getIsAutoGenerateId() {
        return isAutoGenerateId;
    }

    public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
        this.isAutoGenerateId = isAutoGenerateId;
    }

	public String getSourceName() {
		if (StringUtils.isNotBlank(source)) {
			sourceName = ProcessTaskSourceFactory.getSourceName(source);
		}
		return sourceName;
	}

}
