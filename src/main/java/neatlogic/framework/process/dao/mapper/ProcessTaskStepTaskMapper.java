/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.dao.mapper;

import neatlogic.framework.process.dto.*;
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

    List<ProcessTaskStepTaskUserVo> getStepTaskUserByTaskIdAndUserUuid(@Param("processTaskStepTaskId") Long processTaskStepTaskId, @Param("userUuid") String userUuid);

    ProcessTaskStepTaskUserContentVo getStepTaskUserContentByStepTaskUserId(Long processTaskStepTaskUserId);

    ProcessTaskStepTaskUserAgentVo getProcessTaskStepTaskUserAgentByStepTaskUserId(Long stepTaskUserId);

    List<ProcessTaskStepTaskUserAgentVo> getProcessTaskStepTaskUserAgentListByStepTaskUserIdList(List<Long> stepTaskUserIdList);

    List<ProcessTaskStepTaskUserAgentVo> getProcessTaskStepTaskUserAgentListByStepTaskIdList(List<Long> stepTaskIdList);

    List<ProcessTaskStepTaskUserFileVo> getStepTaskUserFileListByStepTaskUserIdList(List<Long> stepTaskUserIdList);

    int insertTask(ProcessTaskStepTaskVo processTaskStepTaskVo);

    int insertIgnoreTaskUser(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo);

    int insertTaskUser(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo);

    int insertTaskUserContent(ProcessTaskStepTaskUserContentVo processTaskStepTaskUserContentVo);

    int insertProcessTaskStepTaskUserAgent(ProcessTaskStepTaskUserAgentVo processTaskStepTaskUserAgentVo);

    int insertProcessTaskStepTaskUserFile(ProcessTaskStepTaskUserFileVo processTaskStepTaskUserFileVo);

    int updateTask(ProcessTaskStepTaskVo processTaskStepTaskVo);

    int updateTaskUserByTaskIdAndUserUuid(@Param("status") String status, @Param("processTaskStepTaskId") Long processtaskStepTaskId, @Param("userUuid") String userUuid);

    int updateTaskUserById(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo);

    int updateDeleteTaskUserByUserListAndId(@Param("userList") List<String> userList, @Param("processTaskStepTaskId") Long processTaskStepTaskId, @Param("isDelete") Integer isDelete);

    int updateTaskUserContent(@Param("processTaskStepTaskUserContentId") Long processTaskStepTaskUserContentId, @Param("contentHash") String contentHash, @Param("userUuid") String userUuid);

    int updateTaskUserContentById(ProcessTaskStepTaskUserContentVo userContentVo);

    int updateTaskUserIsDeleteByIdList(@Param("idList") List<Long> idList, @Param("isDelete") Integer isDelete);

    int deleteTaskById(Long processTaskStepTaskId);

    int deleteTaskUserByTaskId(Long processTaskStepTaskId);

    int deleteTaskUserContentByTaskId(Long processTaskStepTaskId);

    int deleteProcessTaskStepTaskUserAgentByStepTaskUserId(Long stepTaskUserId);

    int deleteProcessTaskStepTaskUserAgentByStepTaskUserIdList(List<Long> stepTaskUserIdList);

    int deleteProcessTaskStepTaskUserAgentByStepTaskId(Long processTaskStepTaskId);

    int deleteProcessTaskStepTaskUserFile(ProcessTaskStepTaskUserFileVo processTaskStepTaskUserFileVo);
}
