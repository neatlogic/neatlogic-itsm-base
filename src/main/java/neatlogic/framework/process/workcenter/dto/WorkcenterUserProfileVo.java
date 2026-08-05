package neatlogic.framework.process.workcenter.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

public class WorkcenterUserProfileVo {
	@EntityField(name = "nfpwd.workcenteruserprofilevo.entityfield.user_uuid.name", type = ApiParamType.STRING)
	private String user_uuid;
	@EntityField(name = "nfpwd.workcenteruserprofilevo.entityfield.config.name", type = ApiParamType.STRING)
	private String config;
	
	public WorkcenterUserProfileVo() {

	}
	public WorkcenterUserProfileVo(String user_uuid, String config) {
		this.user_uuid = user_uuid;
		this.config = config;
	}
	public String getUser_uuid() {
		return user_uuid;
	}
	public void setUser_uuid(String user_uuid) {
		this.user_uuid = user_uuid;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
}
