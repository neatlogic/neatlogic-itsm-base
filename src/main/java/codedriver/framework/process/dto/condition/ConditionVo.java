package codedriver.framework.process.dto.condition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Objects;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.constvalue.UserType;
import codedriver.framework.notify.dto.ParamMappingVo;
import codedriver.framework.process.condition.core.IProcessTaskCondition;
import codedriver.framework.process.condition.core.ProcessTaskConditionFactory;
import codedriver.framework.process.constvalue.ProcessFieldType;
import codedriver.framework.process.constvalue.ProcessWorkcenterField;
import codedriver.framework.process.dto.ProcessTaskStepVo;

public class ConditionVo implements Serializable{
	private static final long serialVersionUID = -776692828809703841L;
	
	private String uuid;
	private String name;
	private String displayName;
	private String type;
	private String handler;
	private JSONObject config;
	private Integer sort;
	private String expression;
	private List<String> valueList;
	
	private List<ParamMappingVo> paramMappingList;
	
	public ConditionVo() {
		super();
	}
	
	public ConditionVo(JSONObject jsonObj) {
		JSONArray paramMappingArray = jsonObj.getJSONArray("paramMappingList");
		if(CollectionUtils.isNotEmpty(paramMappingArray)) {
			paramMappingList = JSON.parseArray(paramMappingArray.toJSONString(), ParamMappingVo.class);
		}
		this.uuid = jsonObj.getString("uuid");
		this.name = jsonObj.getString("name");
		this.type = jsonObj.getString("type");
		this.handler = jsonObj.getString("handler");
		this.expression = jsonObj.getString("expression");
		String values = jsonObj.getString("valueList").replaceAll(GroupSearch.COMMON.getValuePlugin()+UserType.LOGIN_USER.getValue(), GroupSearch.USER.getValuePlugin()+UserContext.get().getUserUuid());
		if(values.startsWith("[") && values.endsWith("]")) {
			this.valueList = JSON.parseArray(values,String.class);
		}else {
			this.valueList = new ArrayList<>();
			this.valueList.add(values);
		}
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDisplayName() {
		return displayName;
	}


	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public JSONObject getConfig() {
		return config;
	}


	public void setConfig(JSONObject config) {
		this.config = config;
	}


	public Integer getSort() {
		return sort;
	}


	public void setSort(Integer sort) {
		this.sort = sort;
	}


	public String getExpression() {
		return expression;
	}


	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public List<String> getValueList() {
		return valueList;
	}

	public void setValueList(List<String> valueList) {
		this.valueList = valueList;
	}

	public List<ParamMappingVo> getParamMappingList() {
		return paramMappingList;
	}

	public void setParamMappingList(List<ParamMappingVo> paramMappingList) {
		this.paramMappingList = paramMappingList;
	}

	public boolean predicate(ProcessTaskStepVo currentProcessTaskStepVo) {
		IProcessTaskCondition workcenterCondition = null;
		if(ProcessFieldType.CUSTOM.getValue().equals(this.type)) {
			if(CollectionUtils.isNotEmpty(paramMappingList)) {
				for(ParamMappingVo paramMappingVo : paramMappingList) {
					if(Objects.equal(this.name, paramMappingVo.getName())) {
						this.type = paramMappingVo.getType();
						this.name = paramMappingVo.getValue();
					}
				}
			}else {
				
			}
		}
		if(ProcessFieldType.COMMON.getValue().equals(this.type)) {
			workcenterCondition = ProcessTaskConditionFactory.getHandler(this.name);
		}else if(ProcessFieldType.FORM.getValue().equals(this.type)) {
			workcenterCondition = ProcessTaskConditionFactory.getHandler(this.type);
		}else if(ProcessFieldType.CONSTANT.getValue().equals(this.type)) {
			workcenterCondition = ProcessTaskConditionFactory.getHandler(this.type);
		}
		if(workcenterCondition != null) {
			return workcenterCondition.predicate(currentProcessTaskStepVo, this);
		}
		return false;
	}

	public String getConditionValue() {
		return ProcessWorkcenterField.getConditionValue(name);
	}
	
	
}
