package neatlogic.framework.process.dto.score;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.common.dto.BaseEditorVo;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessScoreTemplateVo extends BaseEditorVo {

	@EntityField(name = "nfpds.processscoretemplatevo.entityfield.processuuid.name", type = ApiParamType.STRING)
	private String processUuid;
	@EntityField(name = "nfpds.processscoretemplatevo.entityfield.scoretemplateid.name", type = ApiParamType.LONG)
	private Long scoreTemplateId;
//	@EntityField(name = "nfpds.processscoretemplatevo.entityfield.isactive.name", type = ApiParamType.INTEGER)
//	private Integer isActive;
	@EntityField(name = "nfpds.processscoretemplatevo.entityfield.config.name", type = ApiParamType.STRING)
	private String config;

	@EntityField(name = "nfpds.processscoretemplatevo.entityfield.isauto.name", type = ApiParamType.INTEGER)
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
