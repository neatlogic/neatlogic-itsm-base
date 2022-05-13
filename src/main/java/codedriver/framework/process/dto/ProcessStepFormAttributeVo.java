package codedriver.framework.process.dto;

import java.io.Serializable;
import java.util.Objects;

public class ProcessStepFormAttributeVo implements Serializable {
	private static final long serialVersionUID = -6435866167443319573L;
	private String processUuid;
	private String processStepUuid;
	private String formUuid;
	private String attributeUuid;
	private String action;
	private String type;

	public ProcessStepFormAttributeVo() {

	}

	public String getProcessStepUuid() {
		return processStepUuid;
	}

	public void setProcessStepUuid(String processStepUuid) {
		this.processStepUuid = processStepUuid;
	}

	public String getAttributeUuid() {
		return attributeUuid;
	}

	public void setAttributeUuid(String attributeUuid) {
		this.attributeUuid = attributeUuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getProcessUuid() {
		return processUuid;
	}

	public void setProcessUuid(String processUuid) {
		this.processUuid = processUuid;
	}

	public String getFormUuid() {
		return formUuid;
	}

	public void setFormUuid(String formUuid) {
		this.formUuid = formUuid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProcessStepFormAttributeVo that = (ProcessStepFormAttributeVo) o;
		return processStepUuid.equals(that.processStepUuid) && attributeUuid.equals(that.attributeUuid) && action.equals(that.action);
	}

	@Override
	public int hashCode() {
		return Objects.hash(processStepUuid, attributeUuid, action);
	}
}
