package neatlogic.framework.process.dao.mapper.score;

import neatlogic.framework.process.dto.score.ProcessTaskAutoScoreVo;
import neatlogic.framework.process.dto.score.ProcessTaskScoreVo;

import java.util.List;

public interface ProcessTaskScoreMapper {

    List<ProcessTaskScoreVo> getProcessTaskScoreByProcesstaskId(Long processtaskId);

    List<ProcessTaskScoreVo> getProcessTaskScoreWithContentHashByProcessTaskId(Long processtaskId);

    List<Long> getAllProcessTaskAutoScoreProcessTaskIdList();
    
    String getProcessTaskAutoScoreConfigByProcessTaskId(Long processTaskId);

    String getProcessTaskScoreContentHashByProcessTaskId(Long processTaskId);

    void insertProcessTaskScore(ProcessTaskScoreVo vo);

    void insertProcessTaskScoreContent(ProcessTaskScoreVo vo);
    
    int insertProcessTaskAutoScore(ProcessTaskAutoScoreVo processTaskAutoScoreVo);
    
    int updateProcessTaskAutoScoreByProcessTaskId(ProcessTaskAutoScoreVo processTaskAutoScoreVo);
    
    int deleteProcessTaskByProcessTaskId(Long processTaskId);

    int deleteProcessTaskAutoScoreByProcessTaskId(Long processTaskId);
}
