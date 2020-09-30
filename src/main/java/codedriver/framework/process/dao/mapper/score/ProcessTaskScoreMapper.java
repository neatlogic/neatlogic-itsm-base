package codedriver.framework.process.dao.mapper.score;

import java.util.List;

import codedriver.framework.process.dto.score.ProcessTaskScoreVo;

public interface ProcessTaskScoreMapper {

    public List<ProcessTaskScoreVo> searchProcesstaskScoreByProcesstaskId(Long processtaskId);

    public void insertProcesstaskScore(ProcessTaskScoreVo vo);

    public void insertProcesstaskScoreContent(ProcessTaskScoreVo vo);
    
    public int deleteProcessTaskByProcessTaskId(Long processTaskId);

}
