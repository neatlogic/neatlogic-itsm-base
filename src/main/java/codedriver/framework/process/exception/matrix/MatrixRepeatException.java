package codedriver.framework.process.exception.matrix;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class MatrixRepeatException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -9118655742835275741L;

	public MatrixRepeatException(String name) {
		super("矩阵名 '"+name+"' 已存在");
	}
}
