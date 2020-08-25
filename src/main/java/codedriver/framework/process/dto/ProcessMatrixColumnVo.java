package codedriver.framework.process.dto;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

import codedriver.framework.process.constvalue.ProcessMatrixAttributeType;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-04-10 18:25
 **/
public class ProcessMatrixColumnVo {
    private String column;
    private Object value;
    private String expression;
    private String type;

    public ProcessMatrixColumnVo() {
	}

	public ProcessMatrixColumnVo(String column, String value) {
		this.column = column;
		this.value = value;
	}

	public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
    	if(value != null && type != null) {
    		if(value instanceof JSONArray) {
    			JSONArray valueArray = (JSONArray)value;
    			List<String> valueList = new ArrayList<>();
    			for(int i = 0; i < valueArray.size(); i++) {
    				if(ProcessMatrixAttributeType.USER.getValue().equals(type)) {
    					valueList.add(valueArray.getString(i).split("#")[1]);
        			}else if(ProcessMatrixAttributeType.TEAM.getValue().equals(type)) {
        				valueList.add(valueArray.getString(i).split("#")[1]);
        			}else if(ProcessMatrixAttributeType.ROLE.getValue().equals(type)) {
        				valueList.add(valueArray.getString(i).split("#")[1]);
        			}else {
        				valueList.add(valueArray.getString(i));
        			}
    			}
    			return valueList;
    		}else if(value instanceof String){
    			if(ProcessMatrixAttributeType.USER.getValue().equals(type)) {
    				return value = value.toString().split("#")[1];
    			}else if(ProcessMatrixAttributeType.TEAM.getValue().equals(type)) {
    				return value = value.toString().split("#")[1];
    			}else if(ProcessMatrixAttributeType.ROLE.getValue().equals(type)) {
    				return value = value.toString().split("#")[1];
    			}
    		}
    	}
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
