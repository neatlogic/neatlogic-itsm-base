package neatlogic.framework.process.dto.score;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BasePageVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;

public class ScoreTemplateDimensionVo extends BasePageVo {

	@EntityField(name = "nfpds.scoretemplatedimensionvo.entityfield.id.name", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "nfpds.scoretemplatedimensionvo.entityfield.scoretemplateid.name", type = ApiParamType.LONG)
	private Long scoreTemplateId;
	@EntityField(name = "nfpds.scoretemplatedimensionvo.entityfield.name.name", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "nfpds.scoretemplatedimensionvo.entityfield.description.name", type = ApiParamType.STRING)
	private String description;
	@EntityField(name = "nfpds.scoretemplatedimensionvo.entityfield.score.name", type = ApiParamType.INTEGER)
	private Integer score;

	public ScoreTemplateDimensionVo() {}

	public Long getId() {
		if(id == null){
			id = SnowflakeUtil.uniqueLong();
		}
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getScoreTemplateId() {
		return scoreTemplateId;
	}

	public void setScoreTemplateId(Long scoreTemplateId) {
		this.scoreTemplateId = scoreTemplateId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

}
