package codedriver.framework.process.workcenter.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

public class WorkcenterUserProfileVo {
	@EntityField(name = "用户uuid", type = ApiParamType.STRING)
	private String user_uuid;
	@EntityField(name = "用户个性化配置，如排序等", type = ApiParamType.STRING)
	private String config;
	
	public WorkcenterUserProfileVo() {

	}
	public WorkcenterUserProfileVo(String user_uuid, String config) {
		this.user_uuid = user_uuid;
		this.config = config;
	}
	public String getUser_id() {
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
