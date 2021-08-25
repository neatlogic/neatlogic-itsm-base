package codedriver.framework.process.condition.core;

import codedriver.framework.condition.core.IConditionHandler;
import codedriver.framework.dto.condition.ConditionVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.workcenter.dto.JoinTableColumnVo;
import codedriver.framework.process.workcenter.dto.WorkcenterVo;

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
	 * @Description: 根据conditionVo 转成对应的sql
	 * @Author: 89770
	 * @Date: 2021/1/20 18:27
	 * @Params: []
	 * @Returns: void
	 **/
	public void getSqlConditionWhere(List<ConditionVo> conditionList,Integer index,StringBuilder sqlSb);

	/**
	 * @Description: 获取对应条件需要关联的表和字段
	 * @Author: 89770
	 * @Date: 2021/1/22 16:59
	 * @Params: * @param null:
	 * @Returns: * @return: null
	 **/
	public List<JoinTableColumnVo> getJoinTableColumnList(WorkcenterVo workcenterVo);

	/**
	 * 获取条件分流需要判断的数据
	 * @return 数据
	 */
	default Object getConditionParamData(ProcessTaskVo processTaskVo){
		return null;
	}

}
