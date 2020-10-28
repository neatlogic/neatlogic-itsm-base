package codedriver.framework.process.condition.core;

import java.util.List;
import codedriver.framework.condition.core.IConditionHandler;
import codedriver.framework.dto.condition.ConditionVo;

public interface IProcessTaskCondition extends IConditionHandler { 
	
	/**
	 * @Description: 拼接ES where条件
	 * @Param: 
	 * @return: boolean
	 * @Date: 2020/4/13
	 */
	public String  getEsWhere(List<ConditionVo> conditionList,Integer index);
	
	/**
	* @Author 89770
	* @Time 2020年10月19日  
	* @Description: 拼接ES orderby
	* @Param 
	* @return
	 */
	public String  getEsOrderBy(String sort);
	
	/**
	 * 
	* @Author 89770
	* @Time 2020年10月26日  
	* @Description: 获取es 字段 path
	* @Param 
	* @return
	 */
	public String  getEsPath(String... values);
	
	/**
     * 
    * @Author 89770
    * @Time 2020年10月26日  
    * @Description: 获取es 字段 name
    * @Param 
    * @return
     */
    public String  getEsName(String... values);
    
}
