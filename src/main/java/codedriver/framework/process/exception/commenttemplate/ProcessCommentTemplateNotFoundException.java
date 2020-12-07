package codedriver.framework.process.exception.commenttemplate;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ProcessCommentTemplateNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = -6474202454479285132L;

	public ProcessCommentTemplateNotFoundException(Long id) {
		super("回复模版：" + id + "不存在");
	}


}
