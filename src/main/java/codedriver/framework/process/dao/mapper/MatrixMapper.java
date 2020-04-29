package codedriver.framework.process.dao.mapper;

import codedriver.framework.process.dto.ProcessMatrixDispatcherVo;
import codedriver.framework.process.dto.ProcessMatrixFormComponentVo;
import codedriver.framework.process.dto.ProcessMatrixVo;

import java.util.List;

public interface MatrixMapper {
    public int insertMatrix(ProcessMatrixVo matrixVo);

    public int insertMatrixFormComponent(ProcessMatrixFormComponentVo componentVo);

    public int insertMatrixDispatcher(ProcessMatrixDispatcherVo dispatcherVo);

    public int searchMatrixCount(ProcessMatrixVo matrixVo);

    public ProcessMatrixVo getMatrixByUuid(String uuid);

    public ProcessMatrixVo getMatrixByName(String name);

    public List<ProcessMatrixVo> searchMatrix(ProcessMatrixVo matrixVo);

	public List<ProcessMatrixDispatcherVo> getMatrixDispatcherByMatrixUuid(String matrixUuid);

	public List<ProcessMatrixFormComponentVo> getMatrixFormComponentByMatrixUuid(String matrixUuid);

	public int checkMatrixIsExists(String uuid);

	public int checkMatrixNameIsRepeat(ProcessMatrixVo matrixVo);

    public int deleteMatrixByUuid(String uuid);

    public int updateMatrixNameAndLcu(ProcessMatrixVo matrixVo);
}
