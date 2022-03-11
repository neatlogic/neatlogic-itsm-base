package codedriver.framework.process.condition.core;

import codedriver.framework.condition.core.IConditionHandler;
import codedriver.framework.dto.condition.ConditionVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.dto.SqlDecoratorVo;
import codedriver.framework.process.workcenter.dto.JoinTableColumnVo;

import java.util.List;

public interface IProcessTaskCondition extends IConditionHandler {

    /**
     * @Description: 根据conditionVo 转成对应的sql
     * @Author: 89770
     * @Date: 2021/1/20 18:27
     * @Params: []
     * @Returns: void
     **/
    void getSqlConditionWhere(List<ConditionVo> conditionList, Integer index, StringBuilder sqlSb);

    /**
     * @Description: 获取对应条件需要关联的表和字段
     * @Author: 89770
     * @Date: 2021/1/22 16:59
     * @Params: * @param null:
     * @Returns: * @return: null
     **/
    List<JoinTableColumnVo> getJoinTableColumnList(SqlDecoratorVo sqlDecoratorVo);

    /**
     * 获取条件分流需要判断的数据
     *
     * @return 数据
     */
    default Object getConditionParamData(ProcessTaskVo processTaskVo) {
        return null;
    }

}
