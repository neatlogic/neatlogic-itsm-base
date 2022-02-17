/*
 * Copyright(c) 2022 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.crossover;

import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.crossover.ICrossoverService;
import codedriver.framework.dependency.annotation.Dependency;
import codedriver.framework.process.dto.*;
import codedriver.framework.process.dto.score.ProcessScoreTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IProcessCrossoverMapper extends ICrossoverService {
    int checkProcessIsExists(String processUuid);

    List<String> getProcessStepUuidBySlaUuid(String slaUuid);

    ProcessFormVo getProcessFormByProcessUuid(String processUuid);

    List<ProcessStepRelVo> getProcessStepRelByProcessUuid(String processUuid);

    List<ProcessSlaVo> getProcessSlaByProcessUuid(String processUuid);

//    List<String> getSlaUuidListByProcessUuid(String processUuid);

    List<ProcessStepVo> getProcessStepDetailByProcessUuid(String processUuid);

//    List<String> getProcessStepUuidListByProcessUuid(String processUuid);

    ProcessVo getProcessByUuid(String processUuid);

    ProcessVo getProcessByName(String processName);

//    ProcessVo getProcessBaseInfoByUuid(String processUuid);

//    List<ProcessStepVo> searchProcessStep(ProcessStepVo processStepVo);

//    List<ProcessTypeVo> getAllProcessType();

//    int checkProcessNameIsRepeat(ProcessVo processVo);

//    int searchProcessCount(ProcessVo processVo);

//    List<ProcessVo> searchProcessList(ProcessVo processVo);

//    List<ValueTextVo> searchProcessListForSelect(ProcessVo processVo);

//    int getProcessReferenceCount(String processUuid);

//    List<String> getProcessReferenceUuidList(String processUuid);

//    int checkProcessDraftIsExists(ProcessDraftVo processDraftVo);

//    ProcessDraftVo getProcessDraftByUuid(String uuid);

//    List<ProcessDraftVo> getProcessDraftList(ProcessDraftVo processDraftVo);

//    String getEarliestProcessDraft(ProcessDraftVo processDraftVo);

//    List<ProcessStepWorkerPolicyVo> getProcessStepWorkerPolicyListByProcessUuid(String processUuid);

//    ProcessStepVo getProcessStepByUuid(String processStepUuid);

    ProcessScoreTemplateVo getProcessScoreTemplateByProcessUuid(String processUuid);

//    ProcessStepVo getStartProcessStepByProcessUuid(String processUuid);

//    ProcessSlaVo getProcessSlaByUuid(String caller);

    Long getNotifyPolicyIdByProcessStepUuid(String processStepUuid);

//    List<ProcessVo> getProcessListByUuidList(List<String> uuidList);

    List<Long> getProcessStepTagIdListByProcessStepUuid(String processStepUuid);

}
