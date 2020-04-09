package codedriver.framework.process.dao.mapper;

import codedriver.framework.process.dto.ProcessMatrixExternalVo;

public interface MatrixExternalMapper {
    public void insertMatrixExternal(ProcessMatrixExternalVo matrixExternalVo);

    public void updateMatrixExternal(ProcessMatrixExternalVo externalVo);

    public void deleteMatrixExternalByMatrixUuid(String matrixUuid);

    public ProcessMatrixExternalVo getMatrixExternalByMatrixUuid(String matrixUuid);
}
