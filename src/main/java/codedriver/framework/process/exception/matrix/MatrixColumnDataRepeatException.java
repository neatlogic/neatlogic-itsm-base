package codedriver.framework.process.exception.matrix;

import codedriver.framework.process.exception.core.ProcessTaskRuntimeException;

public class MatrixColumnDataRepeatException extends ProcessTaskRuntimeException {

	private static final long serialVersionUID = -9118655742835275741L;

	public MatrixColumnDataRepeatException() {
		super("矩阵数据源：作为“值”的属性列存在相同数据，请修改该列重复数据后重试");
	}
}
