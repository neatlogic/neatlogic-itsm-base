package neatlogic.framework.process.dto.score;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessTaskScoreVo extends BaseEditorVo {

	@EntityField(name = "nfpds.processtaskscorevo.entityfield.processtaskid.name", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "nfpds.processtaskscorevo.entityfield.scoretemplateid.name", type = ApiParamType.LONG)
	private Long scoreTemplateId;
	@EntityField(name = "nfpds.processtaskscorevo.entityfield.scoredimensionid.name", type = ApiParamType.LONG)
	private Long scoreDimensionId;
	@EntityField(name = "nfpds.processtaskscorevo.entityfield.score.name", type = ApiParamType.INTEGER)
	private Integer score;
	@EntityField(name = "nfpds.processtaskscorevo.entityfield.isauto.name", type = ApiParamType.INTEGER)
	private Integer isAuto;
	@EntityField(name = "nfpds.processtaskscorevo.entityfield.contenthash.name", type = ApiParamType.STRING)
	private String contentHash;

	public ProcessTaskScoreVo() {}

	public Long getProcessTaskId() {
		return processTaskId;
	}

	public void setProcessTaskId(Long processTaskId) {
		this.processTaskId = processTaskId;
	}

	public Long getScoreTemplateId() {
		return scoreTemplateId;
	}

	public void setScoreTemplateId(Long scoreTemplateId) {
		this.scoreTemplateId = scoreTemplateId;
	}

	public Long getScoreDimensionId() {
		return scoreDimensionId;
	}

	public void setScoreDimensionId(Long scoreDimensionId) {
		this.scoreDimensionId = scoreDimensionId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(Integer isAuto) {
		this.isAuto = isAuto;
	}

	public String getContentHash() {
		return contentHash;
	}

	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}
}
