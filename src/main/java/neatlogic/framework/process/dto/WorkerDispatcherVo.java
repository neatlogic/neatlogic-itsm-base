package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import com.alibaba.fastjson.JSONArray;

public class WorkerDispatcherVo {
	@EntityField(name = "分派器名称",
			type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "分派器处理类",
			type = ApiParamType.STRING)
	private String handler;
	@EntityField(name = "是否激活",
			type = ApiParamType.INTEGER)
	private Integer isActive;
	@EntityField(name = "输入帮助",
			type = ApiParamType.STRING)
	private String help;
	@EntityField(name = "分派器扩展配置",
			type = ApiParamType.STRING)
	private JSONArray config;
	@EntityField(name = "分派器所属模块",
			type = ApiParamType.STRING)
	private String moduleId;
	@EntityField(name = "是否包含选表单组件",
			type = ApiParamType.INTEGER)
	private int isHasForm = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public JSONArray getConfig() {
		return config;
	}

	public void setConfig(JSONArray config) {
		this.config = config;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public int getIsHasForm() {
		return isHasForm;
	}

	public void setIsHasForm(int isHasForm) {
		this.isHasForm = isHasForm;
	}
}
