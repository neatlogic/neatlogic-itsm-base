package codedriver.framework.process.dto;

import java.io.Serializable;

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

	public ProcessStepFormAttributeVo(String _processStepUuid, String _attributeUuid) {
		this.processStepUuid = _processStepUuid;
		this.attributeUuid = _attributeUuid;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof ProcessStepFormAttributeVo))
			return false;

		final ProcessStepFormAttributeVo attribute = (ProcessStepFormAttributeVo) other;
		try {
			if (getAttributeUuid().equals(attribute.getAttributeUuid())) {
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = getAttributeUuid().hashCode() * 29;
		return result;
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

}
