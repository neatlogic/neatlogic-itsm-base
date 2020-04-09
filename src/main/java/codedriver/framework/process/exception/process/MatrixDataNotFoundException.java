package codedriver.framework.process.exception.process;

import codedriver.framework.exception.core.ApiRuntimeException;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-04-09 11:21
 **/
public class MatrixDataNotFoundException extends ApiRuntimeException {
    private static final long serialVersionUID = -4508274752209783532L;

    public MatrixDataNotFoundException() {
        super("矩阵数据不存在");
    }
}
