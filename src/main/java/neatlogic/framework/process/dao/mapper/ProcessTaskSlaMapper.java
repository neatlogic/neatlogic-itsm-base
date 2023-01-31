/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.dao.mapper;

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

    Long getProcessTaskSlaLockById(Long id);

    List<ProcessTaskSlaTimeVo> getProcessTaskSlaTimeByProcessTaskStepIdList(List<Long> processTaskStepIdList);

    List<ProcessTaskSlaTimeVo> getProcessTaskSlaTimeListBySlaIdList(List<Long> slaIdList);

    ProcessTaskSlaTimeVo getProcessTaskSlaTimeBySlaId(Long slaId);

    List<Long> getSlaIdListByProcessTaskStepId(Long processTaskStepId);

    List<Long> getProcessTaskStepIdListBySlaId(Long slaId);

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
