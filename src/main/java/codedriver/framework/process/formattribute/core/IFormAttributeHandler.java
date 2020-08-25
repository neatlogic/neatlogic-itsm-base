package codedriver.framework.process.formattribute.core;

import com.alibaba.fastjson.JSONObject;

import codedriver.framework.process.dto.AttributeDataVo;
import codedriver.framework.process.exception.form.AttributeValidException;

public interface IFormAttributeHandler {
	/** 下拉列表value和text列的组合连接符 **/
	public final static String SELECT_COMPOSE_JOINER= "&=&";
	
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
