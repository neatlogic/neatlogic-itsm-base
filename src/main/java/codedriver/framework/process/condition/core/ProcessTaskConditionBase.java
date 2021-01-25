package codedriver.framework.process.condition.core;

import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.condition.core.ConditionHandlerFactory;
import codedriver.framework.dto.condition.ConditionVo;
import codedriver.framework.process.constvalue.ProcessFieldType;
import codedriver.framework.process.constvalue.ProcessWorkcenterField;
import codedriver.framework.process.workcenter.dto.JoinTableColumnVo;
import codedriver.framework.process.workcenter.table.ProcessTaskStepUserSqlTable;
import codedriver.framework.process.workcenter.table.ProcessTaskStepWorkerSqlTable;
import codedriver.framework.util.TimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class ProcessTaskConditionBase implements IProcessTaskCondition {

    @Override
    public String getEsWhere(List<ConditionVo> conditionList, Integer index) {
        ConditionVo condition = conditionList.get(index);
        String where = getMyEsWhere(index, conditionList);
        if (StringUtils.isBlank(where)) {
            Object value = StringUtils.EMPTY;
            if (condition.getValueList() instanceof String) {
                value = condition.getValueList();
            } else if (condition.getValueList() instanceof List) {
                List<String> values = JSON.parseArray(JSON.toJSONString(condition.getValueList()), String.class);
                value = String.join("','", values);
            }
            if (StringUtils.isNotBlank(value.toString())) {
                value = String.format("'%s'", value);
            }
            where = String.format(Expression.getExpressionEs(condition.getExpression()),
                    ((IProcessTaskCondition) ConditionHandlerFactory.getHandler(condition.getName())).getEsName(), value);

        }
        return where;
    }

    protected String getMyEsWhere(Integer index, List<ConditionVo> conditionList) {
        return null;
    }

    protected String getDateEsWhere(ConditionVo condition, List<ConditionVo> conditionList) {
        JSONArray dateJSONArray = JSONArray.parseArray(JSON.toJSONString(condition.getValueList()));
        String where = StringUtils.EMPTY;
        if (CollectionUtils.isNotEmpty(dateJSONArray)) {
            JSONObject dateValue = JSONObject.parseObject(dateJSONArray.get(0).toString());
            SimpleDateFormat format = new SimpleDateFormat(TimeUtil.YYYY_MM_DD_HH_MM_SS);
            String startTime = StringUtils.EMPTY;
            String endTime = StringUtils.EMPTY;
            String expression = condition.getExpression();
            if (dateValue.containsKey(ProcessWorkcenterField.STARTTIME.getValue())) {
                startTime = format.format(new Date(dateValue.getLong(ProcessWorkcenterField.STARTTIME.getValue())));
                endTime = format.format(new Date(dateValue.getLong(ProcessWorkcenterField.ENDTIME.getValue())));
            } else {
                startTime = TimeUtil.timeTransfer(dateValue.getInteger("timeRange"), dateValue.getString("timeUnit"));
                endTime = TimeUtil.timeNow();
            }
            if (StringUtils.isEmpty(startTime)) {
                expression = Expression.LESSTHAN.getExpression();
                startTime = endTime;
            } else if (StringUtils.isEmpty(endTime)) {
                expression = Expression.GREATERTHAN.getExpression();
            }
            where = String.format(Expression.getExpressionEs(expression),
                    ((IProcessTaskCondition) ConditionHandlerFactory.getHandler(condition.getName())).getEsName(), startTime,
                    endTime);

        }
        return where;
    }

    @Override
    public String getEsOrderBy(String sortType) {
        if (StringUtils.isBlank(sortType)) {
            return StringUtils.EMPTY;
        }
        String orderBy = getMyEsOrderBy(sortType);
        if (StringUtils.isBlank(orderBy)) {
            orderBy = String.format(" %s %s ", this.getEsPath(), sortType.toUpperCase());
        }
        return orderBy;
    }

    protected String getMyEsOrderBy(String sortType) {
        return StringUtils.EMPTY;
    }

    @Override
    public String getEsName(String... values) {
        String name = String.format("%s.%s ", ProcessFieldType.COMMON.getValue(), this.getName());
        String myName = StringUtils.EMPTY;
        myName = this.getMyEsName();
        if (StringUtils.isBlank(myName)) {
            myName = this.getMyEsName(values);
        }

        if (StringUtils.isNotBlank(myName)) {
            name = myName;
        }
        return name;
    }

    public String getMyEsName(String... values) {
        return null;
    }


    public String getMyEsName() {
        return null;
    }

    @Override
    public String getEsPath(String... values) {
        String esName = getEsName(values);
        return esName;
    }

    protected void getSimpleSqlConditionWhere(ConditionVo condition,StringBuilder sqlSb,String tableShortName,String columnName){
        Object value = StringUtils.EMPTY;
        if (condition.getValueList() instanceof String) {
            value = condition.getValueList();
        } else if (condition.getValueList() instanceof List) {
            List<String> values = JSON.parseArray(JSON.toJSONString(condition.getValueList()), String.class);
            value = String.join("','", values);
        }
        sqlSb.append(Expression.getExpressionSql(condition.getExpression(), tableShortName, columnName, value.toString()));
    }

    public void getDateSqlWhere(ConditionVo condition, StringBuilder sqlSb,String tableShortName,String columnName) {
        JSONArray dateJSONArray = JSONArray.parseArray(JSON.toJSONString(condition.getValueList()));
        if (CollectionUtils.isNotEmpty(dateJSONArray)) {
            JSONObject dateValue = JSONObject.parseObject(dateJSONArray.get(0).toString());
            SimpleDateFormat format = new SimpleDateFormat(TimeUtil.YYYY_MM_DD_HH_MM_SS);
            String startTime = StringUtils.EMPTY;
            String endTime = StringUtils.EMPTY;
            String expression = condition.getExpression();
            if (dateValue.containsKey(ProcessWorkcenterField.STARTTIME.getValue())) {
                startTime = format.format(new Date(dateValue.getLong(ProcessWorkcenterField.STARTTIME.getValue())));
                endTime = format.format(new Date(dateValue.getLong(ProcessWorkcenterField.ENDTIME.getValue())));
            } else {
                startTime = TimeUtil.timeTransfer(dateValue.getInteger("timeRange"), dateValue.getString("timeUnit"));
                endTime = TimeUtil.timeNow();
            }
            if (StringUtils.isEmpty(startTime)) {
                expression = Expression.LESSTHAN.getExpression();
                startTime = endTime;
            } else if (StringUtils.isEmpty(endTime)) {
                expression = Expression.GREATERTHAN.getExpression();
            }
            sqlSb.append(Expression.getExpressionSql(condition.getExpression(), tableShortName, columnName, startTime, endTime));
        }
    }

    @Override
    public List<JoinTableColumnVo> getJoinTableColumnList() {
        return getMyJoinTableColumnList();
    }

    public List<JoinTableColumnVo> getMyJoinTableColumnList() {
        return new ArrayList<>();
    }

    /**
     * @Description: 拼接待处理sql
     * @Author: 89770
     * @Date: 2021/1/25 18:21
     * @Params: [sqlSb, userList, teamList, roleList]
     * @Returns: void
     **/
    protected void getProcessingTaskOfMineSql(StringBuilder sqlSb, List<String> userList, List<String> teamList, List<String> roleList) {
        sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(),new ProcessTaskStepUserSqlTable().getShortName(),ProcessTaskStepUserSqlTable.FieldEnum.USER_UUID.getValue(),String.join("','",userList)));
        //worker-team
        if(CollectionUtils.isNotEmpty(teamList)) {
            sqlSb.append(" or ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.UUID.getValue(), String.join("','", teamList)));
            sqlSb.append(" and ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.TYPE.getValue(), GroupSearch.TEAM.getValue()));
        }
        //worker-role
        if(CollectionUtils.isNotEmpty(roleList)) {
            sqlSb.append(" or ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.UUID.getValue(), String.join("','", roleList)));
            sqlSb.append(" and ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.TYPE.getValue(), GroupSearch.ROLE.getValue()));
        }
        //worker-user
        if(CollectionUtils.isNotEmpty(userList)) {
            sqlSb.append(" or ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.UUID.getValue(), String.join("','", userList)));
            sqlSb.append(" and ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.TYPE.getValue(), GroupSearch.USER.getValue()));
        }
    }
}
