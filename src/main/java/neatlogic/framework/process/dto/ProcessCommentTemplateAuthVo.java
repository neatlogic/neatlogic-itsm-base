package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessCommentTemplateAuthVo{

	@EntityField(name = "nfpd.processcommenttemplateauthvo.entityfield.commenttemplateid.name", type = ApiParamType.LONG)
	private Long commentTemplateId;
	@EntityField(name = "nfpd.processcommenttemplateauthvo.entityfield.type.name", type = ApiParamType.STRING)
	private String type;
	@EntityField(name = "nfpd.processcommenttemplateauthvo.entityfield.uuid.name", type = ApiParamType.STRING)
	private String uuid;

	public ProcessCommentTemplateAuthVo() {}

	public Long getCommentTemplateId() {
		return commentTemplateId;
	}

	public void setCommentTemplateId(Long commentTemplateId) {
		this.commentTemplateId = commentTemplateId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
