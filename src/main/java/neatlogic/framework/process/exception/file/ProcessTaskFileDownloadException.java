package neatlogic.framework.process.exception.file;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessTaskFileDownloadException extends ApiRuntimeException {

	private static final long serialVersionUID = -2268787686377825475L;

	public ProcessTaskFileDownloadException(Long fileId) {
		super("exception.process.processtaskfiledownloadexception", fileId);
	}

}
