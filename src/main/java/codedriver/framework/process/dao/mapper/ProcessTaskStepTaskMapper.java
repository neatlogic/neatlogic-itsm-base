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

    ProcessTaskStepTaskUserVo getStepTaskUserByTaskIdAndTaskUserIdAndUserUuid(@Param("processTaskStepTaskId")Long processtaskStepTaskId,@Param("processTaskStepTaskUserId")Long processTaskStepTaskUserId,@Param("userUuid") String userUuid);

    List<ProcessTaskStepTaskVo> getStepTaskByProcessTaskStepId(Long processTaskStepId);

    List<ProcessTaskStepTaskUserVo> getStepTaskUserByStepTaskIdList(@Param("stepTaskIdList") List<Long> collect);

    List<ProcessTaskStepTaskUserContentVo> getStepTaskUserContentByStepTaskUserIdList(@Param("stepTaskUserIdList") List<Long> collect);

    ProcessTaskStepTaskUserContentVo getStepTaskUserContentById(Long processTaskStepTaskUserContentId);

    List<ProcessTaskStepTaskUserVo> getPendingStepTaskUserListByTaskId(Long processtaskStepTaskId);

    int insertTask(ProcessTaskStepTaskVo processTaskStepTaskVo);

    int insertIgnoreTaskUser(ProcessTaskStepTaskUserVo processTaskStepTaskUserVo);

    int insertTaskUserContent(ProcessTaskStepTaskUserContentVo processTaskStepTaskUserContentVo);

    int updateTask(ProcessTaskStepTaskVo processTaskStepTaskVo);

    int updateTaskUserByTaskIdAndUserUuid(@Param("status") String status, @Param("processtaskStepTaskId") Long processtaskStepTaskId, @Param("userUuid") String userUuid);

    int updateDeleteTaskUserByUserListAndId(@Param("userList") JSONArray userList, @Param("processTaskStepTaskId") Long processTaskStepTaskId,@Param("isDelete") Integer isDelete);

    int updateTaskUserContent(@Param("processTaskStepTaskUserContentId")Long processTaskStepTaskUserContentId,@Param("contentHash") String contentHash,@Param("userUuid") String userUuid);

    int deleteTaskById(Long processTaskStepTaskId);

    int deleteTaskUserByTaskId(Long processTaskStepTaskId);

    int deleteTaskUserContentByTaskId(Long processTaskStepTaskId);

}
