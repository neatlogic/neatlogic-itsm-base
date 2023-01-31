package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.restful.annotation.EntityField;

public class ProcessCommentTemplateUseCountVo {

	@EntityField(name = "回复模版ID", type = ApiParamType.LONG)
	private Long commentTemplateId;
	@EntityField(name = "用户uuid", type = ApiParamType.STRING)
	private String userUuid;
	@EntityField(name = "使用次数", type = ApiParamType.INTEGER)
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
