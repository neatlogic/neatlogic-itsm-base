package codedriver.framework.process.matrixattribute.core;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.dto.ProcessMatrixAttributeVo;

public interface IMatrixAttributeHandler {
	
	public String getType();

	public JSONObject getData(ProcessMatrixAttributeVo processMatrixAttributeVo, String value);
}
