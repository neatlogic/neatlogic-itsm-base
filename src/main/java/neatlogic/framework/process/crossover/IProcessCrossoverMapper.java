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

package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.dto.*;
import neatlogic.framework.process.dto.score.ProcessScoreTemplateVo;

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
