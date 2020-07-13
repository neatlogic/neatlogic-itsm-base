package codedriver.framework.process.formattribute.core;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.dto.AttributeDataVo;
import codedriver.framework.process.exception.form.AttributeValidException;

public interface IFormAttributeHandler {
	public String getType();

	public boolean valid(AttributeDataVo attributeDataVo, JSONObject configObj) throws AttributeValidException;
	/**
	 * 
	* @Time:2020年7月10日
	* @Description: 将表单属性值转换成对应的text
	* @param attributeDataVo
	* @param configObj
	* @return Object
	 */
	public Object valueConversionText(AttributeDataVo attributeDataVo, JSONObject configObj);
}
