package neatlogic.framework.process.dto.score;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessTaskScoreVo extends BaseEditorVo {

	@EntityField(name = "工单ID", type = ApiParamType.LONG)
	private Long processTaskId;
	@EntityField(name = "评分模版ID", type = ApiParamType.LONG)
	private Long scoreTemplateId;
	@EntityField(name = "评分模版维度ID", type = ApiParamType.LONG)
	private Long scoreDimensionId;
	@EntityField(name = "分数", type = ApiParamType.INTEGER)
	private Integer score;
	@EntityField(name = "是否自动评分（0：否，1：是）", type = ApiParamType.INTEGER)
	private Integer isAuto;
	@EntityField(name = "评价内容hash", type = ApiParamType.STRING)
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
