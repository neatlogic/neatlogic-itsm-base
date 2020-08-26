package codedriver.framework.process.dto.score;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BasePageVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;

public class ScoreTemplateDimensionVo extends BasePageVo {

	@EntityField(name = "主键ID", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "评分模版ID", type = ApiParamType.LONG)
	private Long scoreTemplateId;
	@EntityField(name = "评分维度名称", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "评分模版说明", type = ApiParamType.STRING)
	private String description;

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

}
