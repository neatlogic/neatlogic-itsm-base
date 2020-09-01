package codedriver.framework.process.dao.mapper.score;

import codedriver.framework.common.dto.ValueTextVo;
import codedriver.framework.process.dto.score.ScoreTemplateDimensionVo;
import codedriver.framework.process.dto.score.ScoreTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScoreTemplateMapper {

    public List<ScoreTemplateVo> searchScoreTemplate(ScoreTemplateVo scoreTemplateVo);

    public int searchScoreTemplateCount(ScoreTemplateVo scoreTemplateVo);

    public ScoreTemplateVo getScoreTemplateById(@Param("id") Long id);

    public int checkScoreTemplateNameIsRepeat(ScoreTemplateVo scoreTemplateVo);

    public ScoreTemplateVo checkScoreTemplateExistsById(@Param("id") Long id);

    public List<ValueTextVo> getRefProcessList(ScoreTemplateVo scoreTemplateVo);

    public int getRefProcessCount(@Param("scoreTemplateId") Long scoreTemplateId);

    public List<ScoreTemplateVo> getProcessCountByIdList(List<Long> scoreTemplateIdList);

    public Long getScoreTemplateIdByProcessUuid(String processUuid);

    public void updateScoreTemplate(ScoreTemplateVo scoreTemplateVo);

    public void updateScoreTemplateStatus(ScoreTemplateVo scoreTemplateVo);

    public void insertScoreTemplate(ScoreTemplateVo scoreTemplateVo);

    public void insertScoreTemplateDimension(ScoreTemplateDimensionVo scoreTemplateDimensionVo);

    public void deleteScoreTemplate(@Param("scoreTemplateId") Long scoreTemplateId);

    public void deleteScoreTemplateDimension(@Param("scoreTemplateId") Long scoreTemplateId);

}
