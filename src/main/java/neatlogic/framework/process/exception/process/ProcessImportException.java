package neatlogic.framework.process.exception.process;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class ProcessImportException extends ApiRuntimeException {

	private static final long serialVersionUID = -4508274752209783532L;

	public ProcessImportException() {
		super("没有导入文件");
	}

	public ProcessImportException(String fileName) {
		super("没有导入文件", fileName);
	}
}
