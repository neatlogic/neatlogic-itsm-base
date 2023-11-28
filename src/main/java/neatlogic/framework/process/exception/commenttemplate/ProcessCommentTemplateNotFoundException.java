package neatlogic.framework.process.exception.commenttemplate;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessCommentTemplateNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = -6474202454479285132L;

	public ProcessCommentTemplateNotFoundException(Long id) {
		super("回复模版：{0}不存在", id);
	}

	public ProcessCommentTemplateNotFoundException(String name) {
		super("回复模版：{0}不存在", name);
	}


}
