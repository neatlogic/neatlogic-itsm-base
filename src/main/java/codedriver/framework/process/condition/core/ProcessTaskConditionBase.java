package codedriver.framework.process.condition.core;

import codedriver.framework.asynchronization.threadlocal.UserContext;
import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.condition.core.ConditionHandlerFactory;
import codedriver.framework.dao.mapper.UserMapper;
import codedriver.framework.dto.AuthenticationInfoVo;
import codedriver.framework.dto.condition.ConditionVo;
import codedriver.framework.process.constvalue.ConditionConfigType;
import codedriver.framework.process.constvalue.ProcessFieldType;
import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.constvalue.ProcessWorkcenterField;
import codedriver.framework.process.dao.mapper.ProcessTaskAgentMapper;
import codedriver.framework.process.dao.mapper.ProcessTaskMapper;
import codedriver.framework.process.dto.agent.ProcessTaskAgentTargetVo;
import codedriver.framework.process.dto.agent.ProcessTaskAgentVo;
import codedriver.framework.process.service.ProcessTaskAgentService;
import codedriver.framework.process.workcenter.dto.JoinTableColumnVo;
import codedriver.framework.process.workcenter.dto.WorkcenterVo;
import codedriver.framework.process.workcenter.table.ProcessTaskSqlTable;
import codedriver.framework.process.workcenter.table.ProcessTaskStepSqlTable;
import codedriver.framework.process.workcenter.table.ProcessTaskStepUserSqlTable;
import codedriver.framework.process.workcenter.table.ProcessTaskStepWorkerSqlTable;
import codedriver.framework.service.AuthenticationInfoService;
import codedriver.framework.util.TimeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ProcessTaskConditionBase implements IProcessTaskCondition {
    protected static UserMapper userMapper;
    @Resource
    public void setUserMapper(UserMapper _userMapper) {
        userMapper = _userMapper;
    }

    protected static ProcessTaskAgentMapper processTaskAgentMapper;
    @Resource
    public void setProcessTaskAgentMapper(ProcessTaskAgentMapper _processTaskAgentMapper) {
        processTaskAgentMapper = _processTaskAgentMapper;
    }

    protected static ProcessTaskMapper processTaskMapper;
    @Resource
    public void setProcessTaskMapper(ProcessTaskMapper _processTaskMapper) {
        processTaskMapper = _processTaskMapper;
    }

    protected static AuthenticationInfoService authenticationInfoService;

    @Resource
    public void setAuthenticationInfoService(AuthenticationInfoService _authenticationInfoService){
        authenticationInfoService = _authenticationInfoService;
    }

    protected ProcessTaskAgentService processTaskAgentService;

    @Resource
    public void setProcessTaskAgentService(ProcessTaskAgentService _processTaskAgentService){
        processTaskAgentService = _processTaskAgentService;
    }

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

    public void getDateSqlWhereByValueList(ConditionVo condition, StringBuilder sqlSb,String tableShortName,String columnName) {
        JSONArray dateJSONArray = JSONArray.parseArray(JSON.toJSONString(condition.getValueList()));
        if (CollectionUtils.isNotEmpty(dateJSONArray)) {
            JSONObject dateValue = JSONObject.parseObject(dateJSONArray.get(0).toString());
            getDateSqlWhere(dateValue,sqlSb,tableShortName,columnName);
        }
    }

    public void getDateSqlWhere(JSONObject dateValue, StringBuilder sqlSb,String tableShortName,String columnName) {
        SimpleDateFormat format = new SimpleDateFormat(TimeUtil.YYYY_MM_DD_HH_MM_SS);
        String startTime;
        String endTime;
        String expression = Expression.BETWEEN.getExpression();
        if (dateValue.containsKey(ProcessWorkcenterField.STARTTIME.getValuePro())) {
            startTime = format.format(new Date(dateValue.getLong(ProcessWorkcenterField.STARTTIME.getValuePro())));
            endTime = format.format(new Date(dateValue.getLong(ProcessWorkcenterField.ENDTIME.getValuePro())));
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
        sqlSb.append(" ( ");
        sqlSb.append(Expression.getExpressionSql(expression, tableShortName, columnName, startTime, endTime));
        sqlSb.append(" ) ");

    }

    @Override
    public List<JoinTableColumnVo> getJoinTableColumnList(WorkcenterVo workcenterVo) {
        return getMyJoinTableColumnList(workcenterVo);
    }

    public List<JoinTableColumnVo> getMyJoinTableColumnList(WorkcenterVo workcenterVo) {
        return new ArrayList<>();
    }

    /**
     * @Description: 拼接待处理sql
     * @Author: 89770
     * @Date: 2021/1/25 18:21
     * @Params: [sqlSb, userList, teamList, roleList]
     * @Returns: void
     **/
    protected void getProcessingTaskOfMineSqlWhere(StringBuilder sqlSb, List<String> userList, List<String> teamList, List<String> roleList) {
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

    /**
     * @Description: 拼接待我处理条件sql
     * @Author: 89770
     * @Date: 2021/2/1 11:08
     * @Params: [sqlSb]
     * @Returns: void
     **/
    public void getProcessingOfMineConditionSqlWhere(StringBuilder sqlSb){
        List<Long> agentTaskIdList = getAgentProcessTaskId();
        if(CollectionUtils.isNotEmpty(agentTaskIdList)){
            sqlSb.append(" ( ");
        }

        sqlSb.append(" ( ");
        // status
        List<String> statusList = Stream.of(ProcessTaskStatus.RUNNING.getValue())
                .map(String::toString).collect(Collectors.toList());
        sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskSqlTable().getShortName(), ProcessTaskSqlTable.FieldEnum.STATUS.getValue(), String.join("','", statusList)));
        sqlSb.append(" ) and ( ");
        // step.status
        List<String> stepStatusList =
                Stream.of(ProcessTaskStatus.PENDING.getValue(), ProcessTaskStatus.RUNNING.getValue())
                        .map(String::toString).collect(Collectors.toList());
        sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepSqlTable().getShortName(), ProcessTaskStepSqlTable.FieldEnum.STATUS.getValue(), String.join("','", stepStatusList)));
        sqlSb.append(" ) and ( ");
        // step.user
        // 如果是待处理状态，则需额外匹配角色和组
        AuthenticationInfoVo authenticationInfoVo = authenticationInfoService.getAuthenticationInfo(UserContext.get().getUserUuid());
        getProcessingTaskOfMineSqlWhere(sqlSb, Collections.singletonList(UserContext.get().getUserUuid()),authenticationInfoVo.getTeamUuidList(),authenticationInfoVo.getRoleUuidList());
        sqlSb.append(" ) ");

        //agent
        if(CollectionUtils.isNotEmpty(agentTaskIdList)){
            sqlSb.append(" or  ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskSqlTable().getShortName(), ProcessTaskSqlTable.FieldEnum.ID.getValue(), agentTaskIdList.stream().map(Object::toString).collect(Collectors.joining())));
            sqlSb.append(" )");
        }
    }

    /**
     * 获取当前登录人被授权所有可以执行的工单
     * @return 工单idList
     */
    private List<Long> getAgentProcessTaskId(){
        Set<Long> allProcessTaskIdSet = new HashSet<>();
        //1 找出所有当前用户授权记录
        List<ProcessTaskAgentVo> taskAgentVos = processTaskAgentMapper.getProcessTaskAgentDetailListByToUserUuid(UserContext.get().getUserUuid(true));
        //2 循环记录 找出给个授权记录对应的taskIdList 并append
        for(ProcessTaskAgentVo taskAgentVo : taskAgentVos){
            List<ProcessTaskAgentTargetVo> taskAgentTargetVos = taskAgentVo.getProcessTaskAgentTargetVos();
            if(CollectionUtils.isNotEmpty(taskAgentTargetVos)){
                //根据channelUuid找到formUser 所有能处理的工单idList
                List<String> channelUuidList = processTaskAgentService.getChannelUuidListByProcessTaskAgentId(taskAgentVo.getId());
                AuthenticationInfoVo authenticationInfoVo = authenticationInfoService.getAuthenticationInfo(taskAgentVo.getFromUserUuid());
                Set<Long> processTaskIdSet = processTaskMapper.getProcessTaskIdSetByChannelUuidListAndAuthenticationInfo(channelUuidList, authenticationInfoVo);
                allProcessTaskIdSet.addAll(processTaskIdSet);
            }
        }
        return new ArrayList<>(allProcessTaskIdSet);
    }

    @Override
    public JSONObject getConfig(Enum<?> type) {
        if(type instanceof ConditionConfigType) {
            ConditionConfigType configType = (ConditionConfigType) type;
            return  getConfig(configType);
        }else{
            return  getConfig(ConditionConfigType.DEFAULT);
        }
    }

    @Override
    public JSONObject getConfig() {
        return  getConfig(ConditionConfigType.DEFAULT);
    }

    public abstract JSONObject getConfig(ConditionConfigType type);
}
