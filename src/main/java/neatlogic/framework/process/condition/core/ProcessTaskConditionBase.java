/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.condition.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.asynchronization.threadlocal.UserContext;
import neatlogic.framework.common.constvalue.Expression;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.dao.mapper.UserMapper;
import neatlogic.framework.dto.AuthenticationInfoVo;
import neatlogic.framework.dto.condition.ConditionVo;
import neatlogic.framework.process.constvalue.ConditionConfigType;
import neatlogic.framework.process.constvalue.ProcessTaskStatus;
import neatlogic.framework.process.constvalue.ProcessTaskStepStatus;
import neatlogic.framework.process.constvalue.ProcessWorkcenterField;
import neatlogic.framework.process.crossover.IProcessTaskAgentCrossoverMapper;
import neatlogic.framework.process.crossover.IProcessTaskAgentCrossoverService;
import neatlogic.framework.process.crossover.IProcessTaskCrossoverMapper;
import neatlogic.framework.process.dto.SqlDecoratorVo;
import neatlogic.framework.process.dto.agent.ProcessTaskAgentTargetVo;
import neatlogic.framework.process.dto.agent.ProcessTaskAgentVo;
import neatlogic.framework.process.workcenter.dto.JoinTableColumnVo;
import neatlogic.framework.process.workcenter.table.ProcessTaskSqlTable;
import neatlogic.framework.process.workcenter.table.ProcessTaskStepSqlTable;
import neatlogic.framework.process.workcenter.table.ProcessTaskStepWorkerSqlTable;
import neatlogic.framework.service.AuthenticationInfoService;
import neatlogic.framework.util.TimeUtil;
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

    protected static AuthenticationInfoService authenticationInfoService;

    @Resource
    public void setAuthenticationInfoService(AuthenticationInfoService _authenticationInfoService) {
        authenticationInfoService = _authenticationInfoService;
    }

    protected void getSimpleSqlConditionWhere(ConditionVo condition, StringBuilder sqlSb, String tableShortName, String columnName) {
        Object value = StringUtils.EMPTY;
        List<String> valueList = new ArrayList<>();
        if (condition.getValueList() instanceof String) {
            value = condition.getValueList();
        } else if (condition.getValueList() instanceof List) {
            JSONArray values = JSON.parseArray(JSON.toJSONString(condition.getValueList()));
            for (int v = 0; v < values.size(); v++) {
                if (values.get(v) instanceof JSONObject) {
                    JSONObject valueJson = values.getJSONObject(v);
                    valueList.add(valueJson.getString("value"));
                } else {
                    valueList.add(values.getString(v));
                }
            }

            value = String.join("','", valueList);
        }
        sqlSb.append(Expression.getExpressionSql(condition.getExpression(), tableShortName, columnName, value.toString()));
    }

    public void getDateSqlWhereByValueList(ConditionVo condition, StringBuilder sqlSb, String tableShortName, String columnName) {
        JSONArray dateJSONArray = JSON.parseArray(JSON.toJSONString(condition.getValueList()));
        if (CollectionUtils.isNotEmpty(dateJSONArray)) {
            JSONObject dateValue = JSON.parseObject(dateJSONArray.get(0).toString());
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
        List<String> workUuidList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(teamList)) {
            workUuidList.addAll(teamList);
        }
        if (CollectionUtils.isNotEmpty(roleList)) {
            workUuidList.addAll(roleList);
        }
        if (CollectionUtils.isNotEmpty(userList)) {
            workUuidList.addAll(userList);
        }
        sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepWorkerSqlTable().getShortName(), ProcessTaskStepWorkerSqlTable.FieldEnum.UUID.getValue(), String.join("','", workUuidList)));
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
        List<String> statusList = Stream.of(ProcessTaskStatus.RUNNING.getValue(), ProcessTaskStatus.HANG.getValue())
                .map(String::toString).collect(Collectors.toList());
        sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskSqlTable().getShortName(), ProcessTaskSqlTable.FieldEnum.STATUS.getValue(), String.join("','", statusList)));
        sqlSb.append(" ) and ( ");
        // step.status
        List<String> stepStatusList =
                Stream.of(ProcessTaskStepStatus.PENDING.getValue(), ProcessTaskStepStatus.HANG.getValue(), ProcessTaskStepStatus.RUNNING.getValue())
                        .map(String::toString).collect(Collectors.toList());
        sqlSb.append(Expression.getExpressionSql(Expression.INCLUDE.getExpression(), new ProcessTaskStepSqlTable().getShortName(), ProcessTaskStepSqlTable.FieldEnum.STATUS.getValue(), String.join("','", stepStatusList)));
        sqlSb.append(" ) and ( ");
        // step.user
        // 如果是待处理状态，则需额外匹配角色和组
        AuthenticationInfoVo authenticationInfoVo = UserContext.get().getAuthenticationInfoVo();
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
        IProcessTaskAgentCrossoverMapper processTaskAgentCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskAgentCrossoverMapper.class);
        IProcessTaskCrossoverMapper processTaskCrossoverMapper = CrossoverServiceFactory.getApi(IProcessTaskCrossoverMapper.class);
        Set<Long> allProcessTaskIdSet = new HashSet<>();
        //1 找出所有当前用户授权记录
        List<ProcessTaskAgentVo> taskAgentVos = processTaskAgentCrossoverMapper.getProcessTaskAgentDetailListByToUserUuid(UserContext.get().getUserUuid(true));
        //2 循环记录 找出给个授权记录对应的taskIdList 并append
        for (ProcessTaskAgentVo taskAgentVo : taskAgentVos) {
            List<ProcessTaskAgentTargetVo> taskAgentTargetVos = taskAgentVo.getProcessTaskAgentTargetVos();
            if (CollectionUtils.isNotEmpty(taskAgentTargetVos)) {
                IProcessTaskAgentCrossoverService processTaskAgentCrossoverService = CrossoverServiceFactory.getApi(IProcessTaskAgentCrossoverService.class);
                //根据channelUuid找到formUser 所有能处理的工单idList
                List<String> channelUuidList = processTaskAgentCrossoverService.getChannelUuidListByProcessTaskAgentId(taskAgentVo.getId());
                AuthenticationInfoVo authenticationInfoVo = authenticationInfoService.getAuthenticationInfo(taskAgentVo.getFromUserUuid());
                Set<Long> processTaskIdSet = processTaskCrossoverMapper.getProcessTaskIdSetByChannelUuidListAndAuthenticationInfo(channelUuidList, authenticationInfoVo);
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
