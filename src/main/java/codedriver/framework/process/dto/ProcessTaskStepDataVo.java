package codedriver.framework.process.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public class ProcessTaskStepDataVo {
	private Long processTaskId;
	private Long processTaskStepId;
	private JSONObject data;
	private String type;
	
	public ProcessTaskStepDataVo() {

	}
	public ProcessTaskStepDataVo(Long _processTaskId, Long _processTaskStepId,String _type) {
		this.processTaskId = _processTaskId;
		this.processTaskStepId = _processTaskStepId;
		this.type = _type;
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
	
	
}
