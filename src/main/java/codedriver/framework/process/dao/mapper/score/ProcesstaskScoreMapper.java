package codedriver.framework.process.dao.mapper.score;

import codedriver.framework.process.dto.score.ProcesstaskScoreVo;

import java.util.List;

public interface ProcesstaskScoreMapper {

    public List<ProcesstaskScoreVo> searchProcesstaskScoreByProcesstaskId(Long processtaskId);

    public void insertProcesstaskScore(ProcesstaskScoreVo vo);

    public void insertProcesstaskScoreContent(ProcesstaskScoreVo vo);

}
