package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessCommentTemplateUseCountVo {

	@EntityField(name = "nfpd.processcommenttemplateusecountvo.entityfield.commenttemplateid.name", type = ApiParamType.LONG)
	private Long commentTemplateId;
	@EntityField(name = "nfpd.processcommenttemplateusecountvo.entityfield.useruuid.name", type = ApiParamType.STRING)
	private String userUuid;
	@EntityField(name = "nfpd.processcommenttemplateusecountvo.entityfield.count.name", type = ApiParamType.INTEGER)
	private Integer count;

	public ProcessCommentTemplateUseCountVo() {}

	public Long getCommentTemplateId() {
		return commentTemplateId;
	}

	public void setCommentTemplateId(Long commentTemplateId) {
		this.commentTemplateId = commentTemplateId;
	}

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
