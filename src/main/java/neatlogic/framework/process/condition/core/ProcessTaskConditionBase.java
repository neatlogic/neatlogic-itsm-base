/*
 * Copyright(c) 2022 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.condition.core;

import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.Expression;
import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.dto.AuthenticationInfoVo;
import neatlogic.framework.dto.condition.ConditionVo;
import neatlogic.framework.process.constvalue.ConditionConfigType;
import neatlogic.framework.process.constvalue.ProcessTaskStatus;
import neatlogic.framework.process.constvalue.ProcessWorkcenterField;
import neatlogic.framework.process.dao.mapper.ProcessTaskAgentMapper;
import neatlogic.framework.process.dao.mapper.ProcessTaskMapper;
import neatlogic.framework.process.dto.SqlDecoratorVo;
import neatlogic.framework.process.dto.agent.ProcessTaskAgentTargetVo;
import neatlogic.framework.process.dto.agent.ProcessTaskAgentVo;
import neatlogic.framework.process.service.ProcessTaskAgentService;
import neatlogic.framework.process.workcenter.dto.JoinTableColumnVo;
import neatlogic.framework.process.workcenter.table.ProcessTaskSqlTable;
import neatlogic.framework.process.workcenter.table.ProcessTaskStepSqlTable;
import neatlogic.framework.process.workcenter.table.ProcessTaskStepWorkerSqlTable;
import neatlogic.framework.service.AuthenticationInfoService;
import neatlogic.framework.util.TimeUtil;
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
    public void setAuthenticationInfoService(AuthenticationInfoService _authenticationInfoService) {
        authenticationInfoService = _authenticationInfoService;
    }

    protected ProcessTaskAgentService processTaskAgentService;

    @Resource
    public void setProcessTaskAgentService(ProcessTaskAgentService _processTaskAgentService) {
        processTaskAgentService = _processTaskAgentService;
    }

    protected void getSimpleSqlConditionWhere(ConditionVo condition, StringBuilder sqlSb, String tableShortName, String columnName) {
        Object value = StringUtils.EMPTY;
        if (condition.getValueList() instanceof String) {
            value = condition.getValueList();
        } else if (condition.getValueList() instanceof List) {
            List<String> values = JSON.parseArray(JSON.toJSONString(condition.getValueList()), String.class);
            value = String.join("','", values);
        }
        sqlSb.append(Expression.getExpressionSql(condition.getExpression(), tableShortName, columnName, value.toString()));
    }

    public void getDateSqlWhereByValueList(ConditionVo condition, StringBuilder sqlSb, String tableShortName, String columnName) {
        JSONArray dateJSONArray = JSONArray.parseArray(JSON.toJSONString(condition.getValueList()));
        if (CollectionUtils.isNotEmpty(dateJSONArray)) {
            JSONObject dateValue = JSONObject.parseObject(dateJSONArray.get(0).toString());
            getDateSqlWhere(dateValue, sqlSb, tableShortName, columnName);
        }
    }

    public void getDateSqlWhere(JSONObject dateValue, StringBuilder sqlSb, String tableShortName, String columnName) {
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
    public List<JoinTableColumnVo> getJoinTableColumnList(SqlDecoratorVo sqlDecoratorVo) {
        return getMyJoinTableColumnList(sqlDecoratorVo);
    }

    public List<JoinTableColumnVo> getMyJoinTableColumnList(SqlDecoratorVo sqlDecoratorVo) {
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
        /*sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(),new ProcessTaskStepUserSqlTable().getShortName(),ProcessTaskStepUserSqlTable.FieldEnum.USER_UUID.getValue(),String.join("','",userList)));
        sqlSb.append(" and ");
        sqlSb.append(Expression.getExpressionSql(Expression.EQUAL.getExpression(),new ProcessTaskStepUserSqlTable().getShortName(),ProcessTaskStepUserSqlTable.FieldEnum.STATUS.getValue(), ProcessTaskStepUserStatus.DOING.getValue()));*/
        //worker-team
        boolean isFirst = true;
        if (CollectionUtils.isNotEmpty(teamList)) {
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.UUID.getValue(), String.join("','", teamList)));
            sqlSb.append(" and ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.TYPE.getValue(), GroupSearch.TEAM.getValue()));
            isFirst = false;
        }
        //worker-role
        if (CollectionUtils.isNotEmpty(roleList)) {
            if (!isFirst) {
                sqlSb.append(" or ");
            }
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.UUID.getValue(), String.join("','", roleList)));
            sqlSb.append(" and ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.TYPE.getValue(), GroupSearch.ROLE.getValue()));
            isFirst = false;
        }
        //worker-user
        if (CollectionUtils.isNotEmpty(userList)) {
            if (!isFirst) {
                sqlSb.append(" or ");
            }
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
    public void getProcessingOfMineConditionSqlWhere(StringBuilder sqlSb) {
        List<Long> agentTaskIdList = getAgentProcessTaskId();
        if (CollectionUtils.isNotEmpty(agentTaskIdList)) {
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
        getProcessingTaskOfMineSqlWhere(sqlSb, Collections.singletonList(UserContext.get().getUserUuid()), authenticationInfoVo.getTeamUuidList(), authenticationInfoVo.getRoleUuidList());
        sqlSb.append(" ) ");

        //agent
        if (CollectionUtils.isNotEmpty(agentTaskIdList)) {
            sqlSb.append(" or  ");
            sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskSqlTable().getShortName(), ProcessTaskSqlTable.FieldEnum.ID.getValue(), agentTaskIdList.stream().map(Object::toString).collect(Collectors.joining("','"))));
            sqlSb.append(" )");
        }
    }

    /**
     * 获取当前登录人被授权所有可以执行的工单
     *
     * @return 工单idList
     */
    private List<Long> getAgentProcessTaskId() {
        Set<Long> allProcessTaskIdSet = new HashSet<>();
        //1 找出所有当前用户授权记录
        List<ProcessTaskAgentVo> taskAgentVos = processTaskAgentMapper.getProcessTaskAgentDetailListByToUserUuid(UserContext.get().getUserUuid(true));
        //2 循环记录 找出给个授权记录对应的taskIdList 并append
        for (ProcessTaskAgentVo taskAgentVo : taskAgentVos) {
            List<ProcessTaskAgentTargetVo> taskAgentTargetVos = taskAgentVo.getProcessTaskAgentTargetVos();
            if (CollectionUtils.isNotEmpty(taskAgentTargetVos)) {
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
        if (type instanceof ConditionConfigType) {
            ConditionConfigType configType = (ConditionConfigType) type;
            return getConfig(configType);
        } else {
            return getConfig(ConditionConfigType.DEFAULT);
        }
    }

    @Override
    public JSONObject getConfig() {
        return getConfig(ConditionConfigType.DEFAULT);
    }

    public abstract JSONObject getConfig(ConditionConfigType type);
}
