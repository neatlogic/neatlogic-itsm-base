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
    @EntityField(name = "nfpd.processstephandlervo.entityfield.type.name", type = ApiParamType.STRING)
	private String type;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.handler.name", type = ApiParamType.STRING)
	private String handler;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.name.name", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.moduleid.name", type = ApiParamType.STRING)
	private String moduleId;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.isactive.name", type = ApiParamType.STRING)
	private Integer isActive;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.isallowstart.name", type = ApiParamType.STRING)
	private Integer isAllowStart;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.icon.name", type = ApiParamType.STRING)
	private String icon;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.sort.name", type = ApiParamType.INTEGER)
	private Integer sort;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.config.name", type = ApiParamType.STRING)
	private JSONObject config;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.chartconfig.name", type = ApiParamType.JSONOBJECT)
	private JSONObject chartConfig;
	@JSONField(serialize=false)
	private String configStr;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.forwardinputquantity.name", type = ApiParamType.INTEGER)
	private int forwardInputQuantity;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.forwardoutputquantity.name", type = ApiParamType.INTEGER)
	private int forwardOutputQuantity;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.backwardinputquantity.name", type = ApiParamType.INTEGER)
	private int backwardInputQuantity;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.backwardoutputquantity.name", type = ApiParamType.INTEGER)
	private int backwardOutputQuantity;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.ishidden.name", type = ApiParamType.BOOLEAN)
	private Boolean isHidden;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.isfitmobile.name", type = ApiParamType.BOOLEAN)
	private Boolean isFitMobile;
	@EntityField(name = "nfpd.processstephandlervo.entityfield.allowdispatchstepworker.name", type = ApiParamType.BOOLEAN)
	private Boolean allowDispatchStepWorker;

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

	public Boolean getFitMobile() {
		return isFitMobile;
	}

	public void setFitMobile(Boolean fitMobile) {
		isFitMobile = fitMobile;
	}

	public Boolean getAllowDispatchStepWorker() {
		return allowDispatchStepWorker;
	}

	public void setAllowDispatchStepWorker(Boolean allowDispatchStepWorker) {
		this.allowDispatchStepWorker = allowDispatchStepWorker;
	}
}
