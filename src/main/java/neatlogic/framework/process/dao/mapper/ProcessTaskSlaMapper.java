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

import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.process.dto.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author linbq
 * @since 2021/11/28 15:53
 **/
public interface ProcessTaskSlaMapper {

    List<ProcessTaskSlaTransferVo> getAllProcessTaskSlaTransfer();

    ProcessTaskSlaTransferVo getProcessTaskSlaTransferById(Long id);

    List<ProcessTaskSlaTransferVo> getProcessTaskSlaTransferBySlaId(Long slaId);

    List<ProcessTaskSlaNotifyVo> getAllProcessTaskSlaNotify();

    ProcessTaskSlaNotifyVo getProcessTaskSlaNotifyById(Long id);

    List<ProcessTaskSlaNotifyVo> getProcessTaskSlaNotifyBySlaId(Long slaId);

    ProcessTaskSlaVo getProcessTaskSlaById(Long id);

    List<Long> getSlaIdListByProcessTaskId(Long processTaskId);

    String getProcessTaskSlaConfigById(Long id);

    ProcessTaskSlaVo getProcessTaskSlaLockById(Long id);

    List<ProcessTaskSlaTimeVo> getProcessTaskSlaTimeByProcessTaskStepIdList(List<Long> processTaskStepIdList);

    List<ProcessTaskSlaTimeVo> getProcessTaskSlaTimeListBySlaIdList(List<Long> slaIdList);

    ProcessTaskSlaTimeVo getProcessTaskSlaTimeBySlaId(Long slaId);

    List<Long> getSlaIdListByProcessTaskStepId(Long processTaskStepId);

    List<Long> getProcessTaskStepIdListBySlaId(Long slaId);

    int getDoingOrPauseSlaIdCountByWorktimeUuid(String worktimeUuid);

    List<Long> getDoingOrPauseSlaIdListByWorktimeUuid(@Param("worktimeUuid") String worktimeUuid, @Param("startNum") int startNum, @Param("pageSize") int pageSize);

    int insertProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskSlaNotifyVo);

    int insertProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

    int insertProcessTaskSla(ProcessTaskSlaVo processTaskSlaVo);

    int insertProcessTaskSlaTime(ProcessTaskSlaTimeVo processTaskSlaTimeVo);

    int insertProcessTaskStepSla(@Param("processTaskStepId") Long processTaskStepId, @Param("slaId") Long slaId);

    int insertProcessTaskStepSlaTime(ProcessTaskStepSlaTimeVo processTaskStepSlaTimeVo);

    int updateProcessTaskSlaTransfer(ProcessTaskSlaTransferVo processTaskSlaTransferVo);

    int updateProcessTaskSlaNotify(ProcessTaskSlaNotifyVo processTaskNotifyVo);

    int updateProcessTaskSlaTime(ProcessTaskSlaTimeVo processTaskSlaTimeVo);

    int updateProcessTaskSlaIsActiveBySlaId(ProcessTaskSlaVo processTaskSlaVo);

    int deleteProcessTaskSlaNotifyById(Long id);

    int deleteProcessTaskSlaTransferById(Long id);

    int deleteProcessTaskSlaTransferBySlaId(Long slaId);

    int deleteProcessTaskSlaNotifyBySlaId(Long slaId);

    int deleteProcessTaskSlaTimeBySlaId(Long slaId);

    int deleteProcessTaskStepSlaTimeBySlaId(Long slaId);
}
