package codedriver.framework.process.exception.file;

import codedriver.framework.exception.core.ApiRuntimeException;

public class ProcessTaskFileDownloadException extends ApiRuntimeException {

	private static final long serialVersionUID = -2268787686377825475L;

	public ProcessTaskFileDownloadException(Long fileId) {
		super("文件（"+fileId+"）下载失败，找不到对应的工单");
	}

	public ProcessTaskFileDownloadException(Long fileId,String channelName) {
		super("文件（"+fileId+"）下载失败，您不是工单干系人或者没有服务的上报权限");
	}

}
