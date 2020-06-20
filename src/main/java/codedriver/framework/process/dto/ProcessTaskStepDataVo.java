package codedriver.framework.process.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import codedriver.framework.util.SnowflakeUtil;

public class ProcessTaskStepDataVo {
	private Long id;
	private Long processTaskId;
	private Long processTaskStepId;
	private JSONObject data;
	private String type;
	private String fcu;
	private transient Boolean isAutoGenerateId = false;
	public ProcessTaskStepDataVo() {

	}
	public ProcessTaskStepDataVo(boolean _isAutoGenerateId) {
		this.isAutoGenerateId = _isAutoGenerateId;
	}
	public ProcessTaskStepDataVo(Long processTaskId, Long processTaskStepId, String type) {
		this.processTaskId = processTaskId;
		this.processTaskStepId = processTaskStepId;
		this.type = type;
		this.isAutoGenerateId = true;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public JSONObject getData() {
		return data;
	}
	public void setData(String data) {
		try {
			this.data = JSONObject.parseObject(data);
		} catch (Exception ex) {

		}
	}
	
	@JSONField(serialize = false)
	public String getDataStr() {
		if (data != null) {
			return data.toJSONString();
		}
		return null;
	}
	public String getFcu() {
		return fcu;
	}
	public void setFcu(String fcu) {
		this.fcu = fcu;
	}
	public Boolean getIsAutoGenerateId() {
		return isAutoGenerateId;
	}
	public void setIsAutoGenerateId(Boolean isAutoGenerateId) {
		this.isAutoGenerateId = isAutoGenerateId;
	}

	
}
