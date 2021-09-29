package codedriver.framework.process.dao.mapper;

import java.util.List;

import codedriver.framework.process.dto.ProcessTaskStepSubtaskContentVo;
import codedriver.framework.process.dto.ProcessTaskStepSubtaskVo;

@Deprecated
public interface ProcessTaskStepSubtaskMapper {

    public ProcessTaskStepSubtaskVo getProcessTaskStepSubtaskById(Long processTaskStepSubtaskId);

    public List<ProcessTaskStepSubtaskVo> getProcessTaskStepSubtaskListByProcessTaskStepId(Long processTaskStepId);

    public List<ProcessTaskStepSubtaskContentVo> getProcessTaskStepSubtaskContentBySubtaskId(Long processTaskStepSubtaskId);

    public int insertProcessTaskStepSubtask(ProcessTaskStepSubtaskVo processTaskStepSubtaskVo);

    public int insertProcessTaskStepSubtaskContent(ProcessTaskStepSubtaskContentVo processTaskStepSubtaskContentVo);

    public int updateProcessTaskStepSubtaskStatus(ProcessTaskStepSubtaskVo processTaskStepSubtaskVo);

    public int updateProcessTaskStepSubtaskContent(ProcessTaskStepSubtaskContentVo processTaskStepSubtaskContentVo);

}
