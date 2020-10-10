package codedriver.framework.process.dao.mapper.score;

import java.util.List;

import codedriver.framework.process.dto.score.ProcessTaskScoreVo;

public interface ProcessTaskScoreMapper {

    public List<ProcessTaskScoreVo> searchProcessTaskScoreByProcesstaskId(Long processtaskId);

    public void insertProcessTaskScore(ProcessTaskScoreVo vo);

    public void insertProcessTaskScoreContent(ProcessTaskScoreVo vo);
    
    public int deleteProcessTaskByProcessTaskId(Long processTaskId);

}
