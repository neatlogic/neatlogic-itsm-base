package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.dto.approve.ProcessTaskApproveEntityConfigVo;
import neatlogic.framework.process.dto.approve.ProcessTaskApproveEntityVo;
import org.apache.ibatis.annotations.Param;

public interface IProcessTaskApproveCrossoverMapper extends ICrossoverService {

    String getApproveStatusByProcessTaskId(Long processTaskId);

    int insertProcessTaskApproveStatus(@Param("processTaskId") Long processTaskId, @Param("approveStatus") String approveStatus);

    int insertProcessTaskApproveEntity(ProcessTaskApproveEntityVo processTaskApproveEntityVo);

    int insertProcessTaskApproveConfig(ProcessTaskApproveEntityConfigVo processTaskApproveEntityConfigVo);
}
