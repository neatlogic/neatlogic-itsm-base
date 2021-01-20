package codedriver.framework.process.condition.core;

import codedriver.framework.condition.core.IConditionHandler;
import codedriver.framework.dto.condition.ConditionVo;
import codedriver.framework.process.workcenter.table.ISqlTable;

import java.util.List;

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

	/**
	 * @Description: 获取关联数据库表
	 * @Author: 89770
	 * @Date: 2021/1/19 20:01
	 * @Params: []
	 * @Returns: java.lang.String
	 **/
	public List<ISqlTable> getSqlTableList();

	/**
	 * @Description: 根据conditionVo 转成对应的sql
	 * @Author: 89770
	 * @Date: 2021/1/20 18:27
	 * @Params: []
	 * @Returns: void
	 **/
	public void getSqlConditionWhere(List<ConditionVo> conditionList,Integer index,StringBuilder sqlSb);
}
