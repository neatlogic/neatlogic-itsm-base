package codedriver.framework.process.dto.score;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.restful.annotation.EntityField;
import codedriver.framework.util.SnowflakeUtil;

import java.util.List;

public class ScoreTemplateVo extends BaseEditorVo {

	@EntityField(name = "主键ID", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "评分模版名称", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "评分模版说明", type = ApiParamType.STRING)
	private String description;
	@EntityField(name = "是否启用", type = ApiParamType.INTEGER)
	private Integer isActive;
	@EntityField(name = "评分维度列表", type = ApiParamType.JSONARRAY)
	private List<ScoreTemplateDimensionVo> dimensionList;

	public ScoreTemplateVo() {}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public List<ScoreTemplateDimensionVo> getDimensionList() {
		return dimensionList;
	}

	public void setDimensionList(List<ScoreTemplateDimensionVo> dimensionList) {
		this.dimensionList = dimensionList;
	}
}
