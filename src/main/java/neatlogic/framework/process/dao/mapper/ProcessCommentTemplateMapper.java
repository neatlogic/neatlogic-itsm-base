package neatlogic.framework.process.dao.mapper;

import neatlogic.framework.process.dto.ProcessCommentTemplateAuthVo;
import neatlogic.framework.process.dto.ProcessCommentTemplateSearchVo;
import neatlogic.framework.process.dto.ProcessCommentTemplateUseCountVo;
import neatlogic.framework.process.dto.ProcessCommentTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessCommentTemplateMapper {

    public ProcessCommentTemplateVo getTemplateById(Long id);

    public ProcessCommentTemplateVo getTemplateByStepUuidAndAuth(@Param("stepUuid") String uuid,@Param("authList") List<String> authList);

    public ProcessCommentTemplateUseCountVo getTemplateUseCount(@Param("templateId") Long id, @Param("userUuid") String uuid);

    int checkTemplateNameIsRepeat(ProcessCommentTemplateVo vo);

    int searchCommentTemplateCount(ProcessCommentTemplateSearchVo searchVo);

    List<ProcessCommentTemplateVo> searchCommentTemplateList(ProcessCommentTemplateSearchVo searchVo);

    List<ProcessCommentTemplateAuthVo> getProcessCommentTemplateAuthListByCommentTemplateId(Long id);

    public int updateTemplate(ProcessCommentTemplateVo vo);

    public int updateTemplateUseCount(@Param("templateId") Long id, @Param("userUuid") String uuid);

    public int insertTemplate(ProcessCommentTemplateVo vo);

    public int batchInsertAuthority(List<ProcessCommentTemplateAuthVo> list);

    public int insertTemplateUseCount(@Param("templateId") Long id, @Param("userUuid") String uuid);

    public int deleteTemplate(Long id);

    public int deleteTemplateAuthority(Long id);

    public int deleteTemplateUsecount(Long id);
}
