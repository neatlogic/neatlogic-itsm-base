package codedriver.framework.process.exception.matrix;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class MatrixNameRepeatException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -9118655742835275741L;

	public MatrixNameRepeatException(String name) {
		super("矩阵名 :'" + name + "'已存在");
	}
}
