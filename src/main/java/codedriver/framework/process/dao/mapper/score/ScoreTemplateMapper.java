package codedriver.framework.process.dao.mapper.score;

import codedriver.framework.process.dto.score.ScoreTemplateDimensionVo;
import codedriver.framework.process.dto.score.ScoreTemplateVo;
import org.apache.ibatis.annotations.Param;

public interface ScoreTemplateMapper {

    public int checkScoreTemplateNameIsRepeat(ScoreTemplateVo scoreTemplateVo);

    public ScoreTemplateVo checkScoreTemplateExistsById(@Param("id") Long id);

    public void updateScoreTemplate(ScoreTemplateVo scoreTemplateVo);

    public void updateScoreTemplateActiveStatus(ScoreTemplateVo scoreTemplateVo);

    public void insertScoreTemplate(ScoreTemplateVo scoreTemplateVo);

    public void insertScoreTemplateDimension(ScoreTemplateDimensionVo scoreTemplateDimensionVo);

    public void deleteScoreTemplate(@Param("scoreTemplateId") Long scoreTemplateId);

    public void deleteScoreTemplateDimension(@Param("scoreTemplateId") Long scoreTemplateId);

}
