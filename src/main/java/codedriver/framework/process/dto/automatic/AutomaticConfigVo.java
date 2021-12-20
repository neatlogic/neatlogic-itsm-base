package codedriver.framework.process.dto.automatic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.process.constvalue.automatic.CallbackType;

public class AutomaticConfigVo {

	private String baseIntegrationUuid; 
	private String baseFailPolicy; 
	private String baseResultTemplate; 
	private JSONObject timeWindowConfig;
	private JSONArray baseParamList;
	private JSONObject baseSuccessConfig;
	private String callbackType;
	private String callbackIntegrationUuid;
	private JSONArray callbackParamList;
	private Integer callbackInterval;
	private String callbackResultTemplate;
	private JSONObject callbackSuccessConfig;
	private JSONObject callbackFailConfig;
//	private Boolean isRequest;
	// TODO 可能没有作用
//	private JSONObject resultJson;
	
	public AutomaticConfigVo() {}
	
	public AutomaticConfigVo(JSONObject config) {
		this.timeWindowConfig = config.getJSONObject("timeWindowConfig");
		JSONObject requestConfig = config.getJSONObject("requestConfig");
		this.baseIntegrationUuid = requestConfig.getString("integrationUuid");
		this.baseParamList = requestConfig.getJSONArray("paramList");
		this.baseSuccessConfig = requestConfig.getJSONObject("successConfig");
		this.baseFailPolicy = requestConfig.getString("failPolicy");
		this.baseResultTemplate = requestConfig.getString("resultTemplate");
		JSONObject callbackConfig = config.getJSONObject("callbackConfig");
		this.callbackType = callbackConfig.getString("type");
		if(CallbackType.INTERVAL.getValue().equals(this.callbackType)) {
			JSONObject callbackConfigConfig =callbackConfig.getJSONObject("config");
			this.callbackIntegrationUuid = callbackConfigConfig.getString("integrationUuid");
			this.callbackParamList = callbackConfigConfig.getJSONArray("paramList");
			this.callbackInterval = callbackConfigConfig.getInteger("interval");
			this.callbackResultTemplate = callbackConfigConfig.getString("resultTemplate");
			this.callbackSuccessConfig = callbackConfigConfig.getJSONObject("successConfig");
			this.callbackFailConfig = callbackConfigConfig.getJSONObject("failConfig");
		}
	}

	public String getBaseIntegrationUuid() {
		return baseIntegrationUuid;
	}

	public void setBaseIntegrationUuid(String baseIntegrationUuid) {
		this.baseIntegrationUuid = baseIntegrationUuid;
	}

	public String getBaseFailPolicy() {
		return baseFailPolicy;
	}

	public void setBaseFailPolicy(String baseFailPolicy) {
		this.baseFailPolicy = baseFailPolicy;
	}

	public String getBaseResultTemplate() {
		return baseResultTemplate;
	}

	public void setBaseResultTemplate(String baseResultTemplate) {
		this.baseResultTemplate = baseResultTemplate;
	}

	public JSONObject getTimeWindowConfig() {
		return timeWindowConfig;
	}

	public void setTimeWindowConfig(JSONObject timeWindowConfig) {
		this.timeWindowConfig = timeWindowConfig;
	}

	public JSONObject getBaseSuccessConfig() {
		if(baseSuccessConfig != null && baseSuccessConfig.containsKey("expression")) {
			baseSuccessConfig.put("expressionName", Expression.getExpressionName(baseSuccessConfig.getString("expression")));
		}
		return baseSuccessConfig;
	}

	public void setBaseSuccessConfig(JSONObject baseSuccessConfig) {
		this.baseSuccessConfig = baseSuccessConfig;
	}

	public String getCallbackType() {
		return callbackType;
	}

	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}

	public String getCallbackIntegrationUuid() {
		return callbackIntegrationUuid;
	}

	public void setCallbackIntegrationUuid(String callbackIntegrationUuid) {
		this.callbackIntegrationUuid = callbackIntegrationUuid;
	}

	public JSONArray getBaseParamList() {
		return baseParamList;
	}

	public void setBaseParamList(JSONArray baseParamList) {
		this.baseParamList = baseParamList;
	}

	public JSONArray getCallbackParamList() {
		return callbackParamList;
	}

	public void setCallbackParamList(JSONArray callbackParamList) {
		this.callbackParamList = callbackParamList;
	}

	public Integer getCallbackInterval() {
		return callbackInterval;
	}

	public void setCallbackInterval(Integer callbackInterval) {
		this.callbackInterval = callbackInterval;
	}

	public String getCallbackResultTemplate() {
		return callbackResultTemplate;
	}

	public void setCallbackResultTemplate(String callbackResultTemplate) {
		this.callbackResultTemplate = callbackResultTemplate;
	}

	public JSONObject getCallbackSuccessConfig() {
		if(callbackSuccessConfig != null && callbackSuccessConfig.containsKey("expression")) {
			callbackSuccessConfig.put("expressionName", Expression.getExpressionName(callbackSuccessConfig.getString("expression")));
		}
		return callbackSuccessConfig;
	}

	public void setCallbackSuccessConfig(JSONObject callbackSuccessConfig) {
		this.callbackSuccessConfig = callbackSuccessConfig;
	}

	public JSONObject getCallbackFailConfig() {
		if(callbackFailConfig != null && callbackFailConfig.containsKey("expression")) {
			callbackFailConfig.put("expressionName", Expression.getExpressionName(callbackFailConfig.getString("expression")));
		}
		return callbackFailConfig;
	}

	public void setCallbackFailConfig(JSONObject callbackFailConfig) {
		this.callbackFailConfig = callbackFailConfig;
	}

//	public Boolean getIsRequest() {
//		return isRequest;
//	}
//
//	public void setIsRequest(Boolean isRequest) {
//		this.isRequest = isRequest;
//	}

	public Boolean getIsHasCallback() {
		if(callbackType.equals(CallbackType.NONE.getValue())) {
			return false;
		}
		return true;
	}

//	public JSONObject getResultJson() {
//		return resultJson;
//	}
//
//	public void setResultJson(JSONObject resultJson) {
//		this.resultJson = resultJson;
//	}
	
}
