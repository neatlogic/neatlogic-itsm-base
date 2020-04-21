package codedriver.framework.process.workcenter.dto;

import codedriver.framework.apiparam.core.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

public class WorkcenterUserProfileVo {
	@EntityField(name = "用户id", type = ApiParamType.STRING)
	private String user_id;
	@EntityField(name = "用户个性化配置，如排序等", type = ApiParamType.STRING)
	private String config;
	
	public WorkcenterUserProfileVo() {

	}
	public WorkcenterUserProfileVo(String user_id, String config) {
		this.user_id = user_id;
		this.config = config;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
}
