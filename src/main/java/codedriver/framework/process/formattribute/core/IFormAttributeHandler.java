package codedriver.framework.process.formattribute.core;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.attribute.exception.AttributeValidException;
import codedriver.framework.process.dto.AttributeDataVo;

public interface IFormAttributeHandler {
	public String getType();

	public boolean valid(AttributeDataVo attributeDataVo, JSONObject configObj) throws AttributeValidException;

	public String getValue(AttributeDataVo attributeDataVo);
}
