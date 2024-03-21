package neatlogic.framework.process.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ProcessStepHandlerVo implements Comparable<ProcessStepHandlerVo>,Serializable,Cloneable {

    private static final long serialVersionUID = -5961832237506853192L;
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
	@JSONField(serialize=false)
	private String configStr;
	@EntityField(name = "正向输入数量", type = ApiParamType.INTEGER)
	private int forwardInputQuantity;
	@EntityField(name = "正向输出数量", type = ApiParamType.INTEGER)
	private int forwardOutputQuantity;
	@EntityField(name = "回退输入数量", type = ApiParamType.INTEGER)
	private int backwardInputQuantity;
	@EntityField(name = "回退输出数量", type = ApiParamType.INTEGER)
	private int backwardOutputQuantity;
	@EntityField(name = "是否隐藏", type = ApiParamType.BOOLEAN)
	private Boolean isHidden;

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
		if (StringUtils.isNotBlank(config)) {
			try {
				this.config = JSON.parseObject(config);
			} catch (JSONException e) {

			}
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
		if (configStr == null && this.config != null) {
			configStr = this.config.toJSONString();
		}
		return configStr;
	}

	public int getForwardInputQuantity() {
		return forwardInputQuantity;
	}

	public void setForwardInputQuantity(int forwardInputQuantity) {
		this.forwardInputQuantity = forwardInputQuantity;
	}

	public int getForwardOutputQuantity() {
		return forwardOutputQuantity;
	}

	public void setForwardOutputQuantity(int forwardOutputQuantity) {
		this.forwardOutputQuantity = forwardOutputQuantity;
	}

	public int getBackwardInputQuantity() {
		return backwardInputQuantity;
	}

	public void setBackwardInputQuantity(int backwardInputQuantity) {
		this.backwardInputQuantity = backwardInputQuantity;
	}

	public int getBackwardOutputQuantity() {
		return backwardOutputQuantity;
	}

	public void setBackwardOutputQuantity(int backwardOutputQuantity) {
		this.backwardOutputQuantity = backwardOutputQuantity;
	}

	@Override
	public ProcessStepHandlerVo clone() throws CloneNotSupportedException {
		return (ProcessStepHandlerVo) super.clone();
	}

	public Boolean getHidden() {
		return isHidden;
	}

	public void setHidden(Boolean hidden) {
		isHidden = hidden;
	}
}
