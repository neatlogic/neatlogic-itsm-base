package codedriver.framework.process.dao.mapper.score;

import java.util.List;

import codedriver.framework.process.dto.score.ProcessTaskAutoScoreVo;
import codedriver.framework.process.dto.score.ProcessTaskScoreVo;

public interface ProcessTaskScoreMapper {

    public List<ProcessTaskScoreVo> searchProcessTaskScoreByProcesstaskId(Long processtaskId);
    
    public List<Long> getAllProcessTaskAutoScoreProcessTaskIdList();
    
    public String getProcessTaskAutoScoreConfigByProcessTaskId(Long processTaskId);

    public void insertProcessTaskScore(ProcessTaskScoreVo vo);

    public void insertProcessTaskScoreContent(ProcessTaskScoreVo vo);
    
    public int insertProcessTaskAutoScore(ProcessTaskAutoScoreVo processTaskAutoScoreVo);
    
    public int updateProcessTaskAutoScoreByProcessTaskId(ProcessTaskAutoScoreVo processTaskAutoScoreVo);
    
    public int deleteProcessTaskByProcessTaskId(Long processTaskId);

    public int deleteProcessTaskAutoScoreByProcessTaskId(Long processTaskId);
}
