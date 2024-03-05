package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.dto.approve.ProcessTaskApproveEntityConfigVo;
import neatlogic.framework.process.dto.approve.ProcessTaskApproveEntityVo;

public interface IProcessTaskApproveCrossoverMapper extends ICrossoverService {

    int insertProcessTaskApproveEntity(ProcessTaskApproveEntityVo processTaskApproveEntityVo);

    int insertProcessTaskApproveConfig(ProcessTaskApproveEntityConfigVo processTaskApproveEntityConfigVo);

    String getProcessTaskApproveEntityConfigByProcessTaskId(Long processTaskId);
}
