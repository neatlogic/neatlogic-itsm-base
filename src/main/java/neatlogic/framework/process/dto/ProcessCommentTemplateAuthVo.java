package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessCommentTemplateAuthVo{

	@EntityField(name = "回复模版ID", type = ApiParamType.LONG)
	private Long commentTemplateId;
	@EntityField(name = "类型", type = ApiParamType.STRING)
	private String type;
	@EntityField(name = "uuid", type = ApiParamType.STRING)
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
