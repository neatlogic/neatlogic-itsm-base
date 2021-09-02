/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dao.mapper;

import codedriver.framework.process.dto.ProcessTaskStepTaskUserContentVo;
import codedriver.framework.process.dto.ProcessTaskStepTaskUserVo;
import codedriver.framework.process.dto.ProcessTaskStepTaskVo;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/1 14:18
 **/
public interface ProcessTaskStepTaskMapper {

    int getInvokedCountByTaskConfigId(Long taskId);

    ProcessTaskStepTaskVo getStepTaskDetailById(Long processTaskStepTaskId);

    ProcessTaskStepTaskVo getStepTaskById(Long processTaskStepTaskId);

    ProcessTaskStepTaskUserVo getStepTaskUserByTaskIdAndUserUuid(Long processtaskStepTaskId, String userUuid);

    List<ProcessTaskStepTaskVo> getStepTaskByProcessTaskStepId(Long processTaskStepId);

    int insertTask(ProcessTaskStepTaskVo processTaskStepTaskVo);

    int insertIgnoreTaskUser(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo);

    int insertTaskUserContent(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo);

    int updateTask(ProcessTaskStepTaskVo processTaskStepTaskVo);

    int updateTaskUserByTaskIdAndUserUuid(@Param("status") String status, @Param("processtaskStepTaskId") Long processtaskStepTaskId, @Param("userUuid") String userUuid);

    int deleteTaskUserByUserListAndId(@Param("userList") JSONArray userList, @Param("processTaskStepTaskId") Long processTaskStepTaskId);

    int deleteTaskById(Long processTaskStepTaskId);

    int deleteTaskUserByTaskId(Long processTaskStepTaskId);

    int deleteTaskUserContentByTaskId(Long processTaskStepTaskId);

    List<ProcessTaskStepTaskUserVo> getStepTaskUserByStepTaskIdList(@Param("stepTaskIdList") List<Long> collect);

    List<ProcessTaskStepTaskUserContentVo> getStepTaskUserContentByStepTaskUserIdList(@Param("stepTaskUserIdList") List<Long> collect);
}
