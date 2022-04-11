/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dao.mapper;

import codedriver.framework.process.dto.ProcessTaskStepTaskUserAgentVo;
import codedriver.framework.process.dto.ProcessTaskStepTaskUserContentVo;
import codedriver.framework.process.dto.ProcessTaskStepTaskUserVo;
import codedriver.framework.process.dto.ProcessTaskStepTaskVo;
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

    ProcessTaskStepTaskVo getStepTaskLockById(Long processTaskStepTaskId);

    ProcessTaskStepTaskUserVo getStepTaskUserByTaskIdAndTaskUserIdAndUserUuid(@Param("processTaskStepTaskId") Long processtaskStepTaskId, @Param("processTaskStepTaskUserId") Long processTaskStepTaskUserId, @Param("userUuid") String userUuid);

    ProcessTaskStepTaskUserVo getStepTaskUserById(Long id);

    List<ProcessTaskStepTaskVo> getStepTaskByProcessTaskStepId(Long processTaskStepId);

    List<ProcessTaskStepTaskVo> getStepTaskListByProcessTaskStepId(Long processTaskStepId);

    List<ProcessTaskStepTaskUserVo> getStepTaskUserByStepTaskIdList(@Param("stepTaskIdList") List<Long> collect);

    List<ProcessTaskStepTaskUserVo> getStepTaskUserByStepTaskIdListAndUserUuid(@Param("stepTaskIdList") List<Long> collect, @Param("userUuid") String userUuid);

    List<ProcessTaskStepTaskVo> getStepTaskWithUserByProcessTaskStepId(Long processTaskStepId);

    List<ProcessTaskStepTaskUserContentVo> getStepTaskUserContentByStepTaskUserIdList(@Param("stepTaskUserIdList") List<Long> collect);

    ProcessTaskStepTaskUserContentVo getStepTaskUserContentByIdAndUserUuid(@Param("userContentId") Long processTaskStepTaskUserContentId, @Param("userUuid") String userUuid);

    ProcessTaskStepTaskUserContentVo getStepTaskUserContentById(Long id);

    List<ProcessTaskStepTaskUserVo> getStepTaskUserListByTaskIdAndStatus(@Param("processtaskStepTaskId") Long processtaskStepTaskId, @Param("status") String status);

    List<ProcessTaskStepTaskUserVo> getStepTaskUserListByStepTaskId(Long processtaskStepTaskId);

    List<ProcessTaskStepTaskUserVo> getStepTaskUserListByProcessTaskStepId(Long processTaskStepId);

    ProcessTaskStepTaskUserVo getStepTaskUserByTaskIdAndUserUuid(@Param("processTaskStepTaskId") Long processTaskStepTaskId, @Param("userUuid") String userUuid);

    ProcessTaskStepTaskUserContentVo getStepTaskUserContentByStepTaskUserId(Long processTaskStepTaskUserId);

    ProcessTaskStepTaskUserAgentVo getProcessTaskStepTaskUserAgentByStepTaskUserId(Long stepTaskUserId);

    List<ProcessTaskStepTaskUserAgentVo> getProcessTaskStepTaskUserAgentListByStepTaskUserIdList(List<Long> stepTaskUserIdList);

    int insertTask(ProcessTaskStepTaskVo processTaskStepTaskVo);

    int insertIgnoreTaskUser(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo);

    int insertTaskUserContent(ProcessTaskStepTaskUserContentVo processTaskStepTaskUserContentVo);

    int insertProcessTaskStepTaskUserAgent(ProcessTaskStepTaskUserAgentVo processTaskStepTaskUserAgentVo);

    int updateTask(ProcessTaskStepTaskVo processTaskStepTaskVo);

    int updateTaskUserByTaskIdAndUserUuid(@Param("status") String status, @Param("processTaskStepTaskId") Long processtaskStepTaskId, @Param("userUuid") String userUuid);

    int updateTaskUserById(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo);

    int updateDeleteTaskUserByUserListAndId(@Param("userList") List<String> userList, @Param("processTaskStepTaskId") Long processTaskStepTaskId, @Param("isDelete") Integer isDelete);

    int updateTaskUserContent(@Param("processTaskStepTaskUserContentId") Long processTaskStepTaskUserContentId, @Param("contentHash") String contentHash, @Param("userUuid") String userUuid);

    int updateTaskUserContentById(ProcessTaskStepTaskUserContentVo userContentVo);

    int deleteTaskById(Long processTaskStepTaskId);

    int deleteTaskUserByTaskId(Long processTaskStepTaskId);

    int deleteTaskUserContentByTaskId(Long processTaskStepTaskId);

    int deleteProcessTaskStepTaskUserAgentByStepTaskUserId(Long stepTaskUserId);
}
