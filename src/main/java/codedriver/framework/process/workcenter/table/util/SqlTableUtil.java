package codedriver.framework.process.workcenter.table.util;

import codedriver.framework.process.constvalue.ProcessTaskStatus;
import codedriver.framework.process.workcenter.dto.JoinOnVo;
import codedriver.framework.process.workcenter.dto.JoinTableColumnVo;
import codedriver.framework.process.workcenter.dto.SelectColumnVo;
import codedriver.framework.process.workcenter.dto.TableSelectColumnVo;
import codedriver.framework.process.workcenter.table.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Title: SqlTableService
 * @Package: codedriver.framework.process.workcenter.table.service
 * @Description: TODO
 * @Author: 89770
 * @Date: 2021/1/26 10:48
 * Copyright(c) 2021 TechSure Co., Ltd. All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 **/
public class SqlTableUtil {

    public static List<JoinTableColumnVo> getStepUserJoinTableSql() {
        return new ArrayList<JoinTableColumnVo>() {
            {
                add(new JoinTableColumnVo(new ProcessTaskSqlTable(), new ProcessTaskStepSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskSqlTable.FieldEnum.ID.getValue(), ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue()));
                }}));
                add(new JoinTableColumnVo(new ProcessTaskStepSqlTable(), new ProcessTaskStepUserSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue(), ProcessTaskStepUserSqlTable.FieldEnum.PROCESSTASK_ID.getValue()));
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.ID.getValue(), ProcessTaskStepUserSqlTable.FieldEnum.PROCESSTASK_STEP_ID.getValue()));
                }}));
                add(new JoinTableColumnVo(new ProcessTaskStepSqlTable(), new ProcessTaskStepWorkerSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue(), ProcessTaskStepWorkerSqlTable.FieldEnum.PROCESSTASK_ID.getValue()));
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.ID.getValue(), ProcessTaskStepWorkerSqlTable.FieldEnum.PROCESSTASK_STEP_ID.getValue()));
                }}));
                add(new JoinTableColumnVo(new ProcessTaskStepUserSqlTable(), new UserTable(), "ptsuser", new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskStepUserSqlTable.FieldEnum.USER_UUID.getValue(), UserTable.FieldEnum.UUID.getValue()));
                }}));
            }
        };
    }

    public static List<JoinTableColumnVo> getExpireTimeJoinTableSql() {
        return new ArrayList<JoinTableColumnVo>() {
            {
                add(new JoinTableColumnVo(new ProcessTaskSqlTable(), new ProcessTaskStepSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskSqlTable.FieldEnum.ID.getValue(), ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue()));
                }}));
                add(new JoinTableColumnVo(new ProcessTaskStepSqlTable(), new ProcessTaskSlaSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue(), ProcessTaskSlaSqlTable.FieldEnum.PROCESSTASK_ID.getValue()));
                }}));
                add(new JoinTableColumnVo(new ProcessTaskStepSqlTable(), new ProcessTaskStepSlaSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.ID.getValue(), ProcessTaskStepSlaSqlTable.FieldEnum.PROCESSTASK_STEP_ID.getValue()));
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.STATUS.getValue(), ProcessTaskStatus.RUNNING.getValue(), true));
                    add(new JoinOnVo(ProcessTaskSqlTable.FieldEnum.STATUS.getValue(), ProcessTaskStatus.RUNNING.getValue(), true, new ProcessTaskSqlTable().getShortName()));
                }}));
                add(new JoinTableColumnVo(new ProcessTaskStepSlaSqlTable(), new ProcessTaskSlaTimeSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskStepSlaSqlTable.FieldEnum.SLA_ID.getValue(), ProcessTaskSlaTimeSqlTable.FieldEnum.SLA_ID.getValue()));
                }}));
            }
        };
    }

    public static List<JoinTableColumnVo> getChannelTypeJoinTableSql() {
        return new ArrayList<JoinTableColumnVo>() {
            {
                add(new JoinTableColumnVo(new ProcessTaskSqlTable(), new ChannelSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskSqlTable.FieldEnum.CHANNEL_UUID.getValue(), ChannelSqlTable.FieldEnum.UUID.getValue()));
                }}));
                add(new JoinTableColumnVo(new ChannelSqlTable(), new ChannelTypeSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ChannelSqlTable.FieldEnum.CHANNEL_TYPE_UUID.getValue(), ChannelTypeSqlTable.FieldEnum.UUID.getValue()));
                }}));
            }
        };
    }

    /**
     * @Description:
     * @Author: 89770
     * @Date: 2021/2/1 18:06
     * @Params: [list]
     * @Returns: void
     **/
    public static List<JoinTableColumnVo> getProcessingOfMineJoinTableSql() {
        return new ArrayList<JoinTableColumnVo>() {
            {
                add(new JoinTableColumnVo(new ProcessTaskSqlTable(), new ProcessTaskStepSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskSqlTable.FieldEnum.ID.getValue(), ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue()));
                }}));
                add(new JoinTableColumnVo(new ProcessTaskStepSqlTable(), new ProcessTaskStepUserSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue(), ProcessTaskStepUserSqlTable.FieldEnum.PROCESSTASK_ID.getValue()));
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.ID.getValue(), ProcessTaskStepUserSqlTable.FieldEnum.PROCESSTASK_STEP_ID.getValue()));
                }}));
                add(new JoinTableColumnVo(new ProcessTaskStepSqlTable(), new ProcessTaskStepWorkerSqlTable(), new ArrayList<JoinOnVo>() {{
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue(), ProcessTaskStepWorkerSqlTable.FieldEnum.PROCESSTASK_ID.getValue()));
                    add(new JoinOnVo(ProcessTaskStepSqlTable.FieldEnum.ID.getValue(), ProcessTaskStepWorkerSqlTable.FieldEnum.PROCESSTASK_STEP_ID.getValue()));
                }}));
            }
        };
    }

    /**
     * @Description: 获取stepUser 相关表字段关系
     * @Author: 89770
     * @Date: 2021/3/11 18:02
     * @Params: []
     * @Returns: java.util.List<codedriver.framework.process.workcenter.dto.TableSelectColumnVo>
     **/
    public static List<TableSelectColumnVo> getTableSelectColumn() {
        return new ArrayList<TableSelectColumnVo>() {
            {
                add(new TableSelectColumnVo(new ProcessTaskStepSqlTable(), Arrays.asList(
                        new SelectColumnVo(ProcessTaskStepSqlTable.FieldEnum.ID.getValue(), "processTaskStepId"),
                        new SelectColumnVo(ProcessTaskStepSqlTable.FieldEnum.NAME.getValue(), "processTaskStepName"),
                        new SelectColumnVo(ProcessTaskStepSqlTable.FieldEnum.PROCESSTASK_ID.getValue(), "processTaskId"),
                        new SelectColumnVo(ProcessTaskStepSqlTable.FieldEnum.HANDLER.getValue(), "processTaskStepHandler")
                )));
                add(new TableSelectColumnVo(new ProcessTaskStepWorkerSqlTable(), Arrays.asList(
                        new SelectColumnVo(ProcessTaskStepWorkerSqlTable.FieldEnum.UUID.getValue(), "stepWorkerUuid"),
                        new SelectColumnVo(ProcessTaskStepWorkerSqlTable.FieldEnum.TYPE.getValue(), "stepWorkerType"),
                        new SelectColumnVo(ProcessTaskStepWorkerSqlTable.FieldEnum.USER_TYPE.getValue(), "stepWorkerUserType")
                )));
                add(new TableSelectColumnVo(new ProcessTaskStepUserSqlTable(), Arrays.asList(
                        new SelectColumnVo(ProcessTaskStepUserSqlTable.FieldEnum.STATUS.getValue(), "stepUserUserStatus"),
                        new SelectColumnVo(ProcessTaskStepUserSqlTable.FieldEnum.USER_TYPE.getValue(), "stepUserUserType")
                )));
                add(new TableSelectColumnVo(new UserTable(), "ptsuser", Arrays.asList(
                        new SelectColumnVo(UserTable.FieldEnum.USER_NAME.getValue(), "stepUserUserName"),
                        new SelectColumnVo(UserTable.FieldEnum.UUID.getValue(), "stepUserUserUuid", true),
                        new SelectColumnVo(UserTable.FieldEnum.USER_INFO.getValue(), "stepUserUserInfo"),
                        new SelectColumnVo(UserTable.FieldEnum.VIP_LEVEL.getValue(), "stepUserUserVipLevel"),
                        new SelectColumnVo(UserTable.FieldEnum.PINYIN.getValue(), "stepUserUserPinyin")
                )));
            }
        };
    }
}
