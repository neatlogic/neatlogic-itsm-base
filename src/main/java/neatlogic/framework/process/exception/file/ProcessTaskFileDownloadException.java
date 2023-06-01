package neatlogic.framework.process.exception.file;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessTaskFileDownloadException extends ApiRuntimeException {

	private static final long serialVersionUID = -2268787686377825475L;

	public ProcessTaskFileDownloadException(Long fileId) {
		super("文件（{0}）下载失败，您不是工单干系人或者没有服务的上报权限", fileId);
	}

}
