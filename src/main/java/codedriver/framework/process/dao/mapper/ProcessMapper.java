package codedriver.framework.process.dao.mapper;

import java.util.List;

import codedriver.framework.common.dto.ValueTextVo;
import org.apache.ibatis.annotations.Param;

import codedriver.framework.process.dto.ChannelProcessVo;
import codedriver.framework.process.dto.ChannelVo;
import codedriver.framework.process.dto.ProcessDraftVo;
import codedriver.framework.process.dto.ProcessFormVo;
import codedriver.framework.process.dto.ProcessSlaVo;
import codedriver.framework.process.dto.ProcessStepFormAttributeVo;
import codedriver.framework.process.dto.ProcessStepNotifyTemplateVo;
import codedriver.framework.process.dto.ProcessStepRelVo;
import codedriver.framework.process.dto.ProcessStepTeamVo;
import codedriver.framework.process.dto.ProcessStepVo;
import codedriver.framework.process.dto.ProcessStepWorkerPolicyVo;
import codedriver.framework.process.dto.ProcessTypeVo;
import codedriver.framework.process.dto.ProcessVo;

public interface ProcessMapper {
	public int checkProcessIsExists(String processUuid);

	public List<String> getProcessStepUuidBySlaUuid(String slaUuid);

	public ProcessFormVo getProcessFormByProcessUuid(String processUuid);

	public List<ProcessStepRelVo> getProcessStepRelByProcessUuid(String processUuid);

	public List<ProcessSlaVo> getProcessSlaByProcessUuid(String processUuid);

	public List<ProcessStepVo> getProcessStepDetailByProcessUuid(String processUuid);

	public List<ProcessStepFormAttributeVo> getProcessStepFormAttributeByStepUuid(String processStepUuid);

	public ProcessVo getProcessByUuid(String processUuid);
	
	public ProcessVo getProcessByName(String processName);

	public ProcessVo getProcessBaseInfoByUuid(String processUuid);

	public List<ProcessStepVo> searchProcessStep(ProcessStepVo processStepVo);

	public List<ProcessTypeVo> getAllProcessType();

	public int checkProcessNameIsRepeat(ProcessVo processVo);

	public int searchProcessCount(ProcessVo processVo);

	public List<ProcessVo> searchProcessList(ProcessVo processVo);

	public List<ValueTextVo> searchProcessListForSelect(ProcessVo processVo);

	public int getProcessReferenceCount(String processUuid);

	public List<ChannelVo> getProcessReferenceList(ChannelProcessVo channelProcessVo);

	public int checkProcessDraftIsExists(ProcessDraftVo processDraftVo);

	public ProcessDraftVo getProcessDraftByUuid(String uuid);

	public List<ProcessDraftVo> getProcessDraftList(ProcessDraftVo processDraftVo);

	public String getEarliestProcessDraft(ProcessDraftVo processDraftVo);

	public List<ProcessStepWorkerPolicyVo> getProcessStepWorkerPolicyListByProcessUuid(String processUuid);

	public ProcessStepVo getProcessStepByUuid(String processStepUuid);

	public int insertProcess(ProcessVo processVo);

	public int insertProcessStep(ProcessStepVo processStepVo);

	public int insertProcessStepFormAttribute(ProcessStepFormAttributeVo processStepFormAttributeVo);

	public int insertProcessStepRel(ProcessStepRelVo processStepRelVo);

	public int insertProcessStepTeam(ProcessStepTeamVo processStepTeamVo);

	public int insertProcessStepWorkerPolicy(ProcessStepWorkerPolicyVo processStepWorkerPolicyVo);

	public int insertProcessForm(ProcessFormVo processFormVo);

	public int insertProcessStepSla(@Param("stepUuid") String stepUuid, @Param("slaUuid") String slaUuid);

	public int insertProcessSla(ProcessSlaVo processSlaVo);

	public int replaceProcessStepNotifyTemplate(ProcessStepNotifyTemplateVo processStepNotifyTemplateVo);

	public int insertProcessDraft(ProcessDraftVo processDraftVo);

	public int updateProcess(ProcessVo processVo);

	public int deleteProcessStepByProcessUuid(String processUuid);

	public int deleteProcessStepRelByProcessUuid(String processUuid);

	public int deleteProcessStepTeamByProcessUuid(String processUuid);

	public int deleteProcessStepWorkerPolicyByProcessUuid(String processUuid);

	public int deleteProcessStepFormAttributeByProcessUuid(String processUuid);

	public int deleteProcessByUuid(String uuid);

	public int deleteProcessFormByProcessUuid(String processUuid);

	public int deleteProcessStepNotifyTemplateByProcessUuid(String processUuid);

	public int deleteProcessDraft(ProcessDraftVo processDraftVo);

	public int deleteProcessDraftByUuid(String uuid);

	public int deleteProcessSlaByProcessUuid(String uuid);

}
