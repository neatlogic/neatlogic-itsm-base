package neatlogic.framework.process.dto.score;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;
import neatlogic.framework.util.SnowflakeUtil;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ScoreTemplateVo extends BaseEditorVo {

	@EntityField(name = "nfpds.scoretemplatevo.entityfield.id.name", type = ApiParamType.LONG)
	private Long id;
	@EntityField(name = "nfpds.scoretemplatevo.entityfield.name.name", type = ApiParamType.STRING)
	private String name;
	@EntityField(name = "nfpds.scoretemplatevo.entityfield.description.name", type = ApiParamType.STRING)
	private String description;
	@EntityField(name = "nfpds.scoretemplatevo.entityfield.isactive.name", type = ApiParamType.INTEGER)
	private Integer isActive;
	@EntityField(name = "nfpds.scoretemplatevo.entityfield.dimensionlist.name", type = ApiParamType.JSONARRAY)
	private List<ScoreTemplateDimensionVo> dimensionList;

	@EntityField(name = "nfpds.scoretemplatevo.entityfield.dimensionnamelist.name", type = ApiParamType.JSONARRAY)
	@JSONField(serialize = false)
	private List<String> dimensionNameList;

	/** 评分维度名称，利用dimensionNameList拼接而成的字符串，分隔符为"、"，用于管理页列表 */
	private String dimensionNames;

	@EntityField(name = "nfpds.scoretemplatevo.entityfield.processcount.name", type = ApiParamType.INTEGER)
	private Integer processCount;


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

	public void setDimensionNameList(List<String> dimensionNameList) {
		this.dimensionNameList = dimensionNameList;
	}

	public String getDimensionNames() {
		if(StringUtils.isBlank(dimensionNames) && CollectionUtils.isNotEmpty(dimensionNameList)){
			dimensionNames = StringUtils.join(dimensionNameList,"、");
		}
		return dimensionNames;
	}

	public Integer getProcessCount() {
		return processCount;
	}

	public void setProcessCount(Integer processCount) {
		this.processCount = processCount;
	}
}
