/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
