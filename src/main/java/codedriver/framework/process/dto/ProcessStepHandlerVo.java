package codedriver.framework.process.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessStepHandlerVo implements Comparable<ProcessStepHandlerVo> {

	@EntityField(name = "类型", type = ApiParamType.STRING)
	private String type;
	@EntityField(name = "处理器", type = ApiParamType.STRING)
	private String handler;
	@EntityField(name = "名称", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "模块id", type = ApiParamType.STRING)
	private String moduleId;
	@EntityField(name = "是否激活", type = ApiParamType.STRING)
	private Integer isActive;
	@EntityField(name = "图标", type = ApiParamType.STRING)
	private Integer isAllowStart;
	@EntityField(name = "是否允许作为开始节点", type = ApiParamType.STRING)
	private String icon;
	@EntityField(name = "排序", type = ApiParamType.INTEGER)
	private Integer sort;
	@EntityField(name = "配置信息", type = ApiParamType.STRING)
	private JSONObject config;
	@EntityField(name = "前端配置信息", type = ApiParamType.JSONOBJECT)
	private JSONObject chartConfig;

	private transient String configStr;
	
	public ProcessStepHandlerVo() {
	}

	public ProcessStepHandlerVo(String handler, String name, JSONObject config) {
		this.handler = handler;
		this.name = name;
		this.config = config;
	}

	public JSONObject getConfig() {
		return config;
	}

	public void setConfig(String config) {
		try {
			this.config = JSON.parseObject(config);
		}catch(JSONException e) {
			
		}		
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	@Override
	public int compareTo(ProcessStepHandlerVo o) {
		return o.getSort() - this.getSort();
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public JSONObject getChartConfig() {
		return chartConfig;
	}

	public void setChartConfig(JSONObject chartConfig) {
		this.chartConfig = chartConfig;
	}

	public Integer getIsAllowStart() {
		return isAllowStart;
	}

	public void setIsAllowStart(Integer isAllowStart) {
		this.isAllowStart = isAllowStart;
	}

	public String getConfigStr() {
		if(configStr != null && this.config != null) {
			configStr = this.config.toJSONString();
		}
		return configStr;
	}
}
