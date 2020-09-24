package codedriver.framework.process.dto.score;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.dto.BaseEditorVo;
import codedriver.framework.restful.annotation.EntityField;

public class ProcessScoreTemplateVo extends BaseEditorVo {

	@EntityField(name = "流程UUID", type = ApiParamType.STRING)
	private String processUuid;
	@EntityField(name = "评分模版ID", type = ApiParamType.LONG)
	private Long scoreTemplateId;
//	@EntityField(name = "是否启用评分（0：否，1：是）", type = ApiParamType.INTEGER)
//	private Integer isActive;
	@EntityField(name = "评分配置", type = ApiParamType.STRING)
	private String config;

	@EntityField(name = "是否自动评分", type = ApiParamType.INTEGER)
	private Integer isAuto;
	
	public ProcessScoreTemplateVo() {}

	public String getProcessUuid() {
		return processUuid;
	}

	public void setProcessUuid(String processUuid) {
		this.processUuid = processUuid;
	}

	public Long getScoreTemplateId() {
		return scoreTemplateId;
	}

	public void setScoreTemplateId(Long scoreTemplateId) {
		this.scoreTemplateId = scoreTemplateId;
	}

//	public Integer getIsActive() {
//		return isActive;
//	}
//
//	public void setIsActive(Integer isActive) {
//		this.isActive = isActive;
//	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

    public Integer getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(Integer isAuto) {
        this.isAuto = isAuto;
    }
}
