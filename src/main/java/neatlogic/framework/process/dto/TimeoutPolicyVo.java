package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

public class TimeoutPolicyVo {

	@EntityField(name = "nfpd.timeoutpolicyvo.entityfield.type.name", type = ApiParamType.STRING)
	private String type;
	@EntityField(name = "nfpd.timeoutpolicyvo.entityfield.name.name", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "nfpd.timeoutpolicyvo.entityfield.moduleid.name", type = ApiParamType.STRING)
	private String moduleId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
}
