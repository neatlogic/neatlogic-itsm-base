package neatlogic.framework.process.dao.mapper;

import neatlogic.framework.common.dto.ValueTextVo;
import neatlogic.framework.process.dto.ProcessCommentTemplateAuthVo;
import neatlogic.framework.process.dto.ProcessCommentTemplateUseCountVo;
import neatlogic.framework.process.dto.ProcessCommentTemplateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessCommentTemplateMapper {

    public int checkTemplateExistsById(Long id);

    public ProcessCommentTemplateVo getTemplateById(Long id);

    public int searchTemplateCount(ProcessCommentTemplateVo vo);

    public int searchTemplateCountForTask(ProcessCommentTemplateVo vo);

    public List<ProcessCommentTemplateVo> searchTemplate(ProcessCommentTemplateVo vo);

    public List<ProcessCommentTemplateVo> searchTemplateForTask(ProcessCommentTemplateVo vo);

    public List<ValueTextVo> searchTemplateForSelect(ProcessCommentTemplateVo vo);

    public ProcessCommentTemplateVo getTemplateByStepUuidAndAuth(@Param("stepUuid") String uuid,@Param("authList") List<String> authList);

    public ProcessCommentTemplateUseCountVo getTemplateUseCount(@Param("templateId") Long id, @Param("userUuid") String uuid);

    public int updateTemplate(ProcessCommentTemplateVo vo);

    public int updateTemplateUseCount(@Param("templateId") Long id, @Param("userUuid") String uuid);

    public int insertTemplate(ProcessCommentTemplateVo vo);

    public int batchInsertAuthority(List<ProcessCommentTemplateAuthVo> list);

    public int insertTemplateUseCount(@Param("templateId") Long id, @Param("userUuid") String uuid);

    public int deleteTemplate(Long id);

    public int deleteTemplateAuthority(Long id);
}
