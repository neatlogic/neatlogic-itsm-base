package codedriver.framework.process.column.core;

import com.alibaba.fastjson.JSONObject;
import com.techsure.multiattrsearch.MultiAttrsObject;

public interface IProcessTaskColumn {
	
	/**
	 * @Description: 字段英文名
	 * @Param:
	 * @return: java.lang.String
	 * @Date: 2020/2/2
	 */
	public String getName();
	
	/**
	 * @Description: 字段显示名
	 * @Param:
	 * @return: java.lang.String
	 * @Date: 2020/2/2
	 */
	public String getDisplayName();
	
	/**
	 * @Description: 字段是否允许排序
	 * @Param:
	 * @return: java.lang.String
	 * @Date: 2020/2/2
	 */
	public Boolean allowSort();

	/**
	 * @Description: 获取显示值 workcenter
	 * @Param: 
	 * @return: java.lang.Object
	 * @Date: 2020/2/2
	 */
	public Object getValue(MultiAttrsObject el) throws RuntimeException;
	
	/**
	 * @Description: 获取显示值 dashboard
	 * @Param: 
	 * @return: java.lang.Object
	 * @Date: 2020/2/2
	 */
	public Object getValueText(MultiAttrsObject el) throws RuntimeException;
	
	/**
	 * @Description: 获取类型
	 * @Param: 
	 * @return: java.lang.String
	 * @Date: 2020/3/25
	 */
	public String getType();
	
	/**
	 * @Description: 获取展示字段样式
	 * @Param: 
	 * @return: java.lang.String
	 * @Date: 2020/3/26
	 */
	public String getClassName();
	
	/**
	 * @Description: 获取展示字段默认顺序
	 * @Param: 
	 * @return: java.lang.String
	 * @Date: 2020/3/27
	 */
	public Integer getSort();
	

	/**
	 * @Description: 是否显示该字段
	 * @Param:
	 * @return: java.lang.String
	 * @Date: 2020/6/5
	 */
	public Boolean getIsShow();

	Object getMyValue(JSONObject json);

}
