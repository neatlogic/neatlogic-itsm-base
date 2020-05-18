package codedriver.framework.process.formattribute.core;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.dto.AttributeDataVo;
import codedriver.framework.process.exception.form.AttributeValidException;

public interface IFormAttributeHandler {
	public String getType();

	public boolean valid(AttributeDataVo attributeDataVo, JSONObject configObj) throws AttributeValidException;

	public String getValue(AttributeDataVo attributeDataVo, JSONObject configObj);
}
