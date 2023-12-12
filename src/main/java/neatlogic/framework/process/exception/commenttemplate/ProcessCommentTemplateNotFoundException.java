package neatlogic.framework.process.exception.commenttemplate;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessCommentTemplateNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = -6474202454479285132L;

	public ProcessCommentTemplateNotFoundException(Long id) {
		super("nfpec.processcommenttemplatenotfoundexception.processcommenttemplatenotfoundexception", id);
	}

	public ProcessCommentTemplateNotFoundException(String name) {
		super("nfpec.processcommenttemplatenotfoundexception.processcommenttemplatenotfoundexception", name);
	}


}
