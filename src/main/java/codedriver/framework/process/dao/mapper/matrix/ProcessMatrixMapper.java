package codedriver.framework.process.dao.mapper.matrix;

import java.util.List;

import codedriver.framework.process.dto.matrix.ProcessMatrixDispatcherVo;
import codedriver.framework.process.dto.matrix.ProcessMatrixFormComponentVo;

public interface ProcessMatrixMapper {

    public List<ProcessMatrixDispatcherVo> getMatrixDispatcherByMatrixUuid(String matrixUuid);

	public List<ProcessMatrixFormComponentVo> getMatrixFormComponentByMatrixUuid(String matrixUuid);
	
	public int insertMatrixFormComponent(ProcessMatrixFormComponentVo componentVo);
	    
	public int insertMatrixDispatcher(ProcessMatrixDispatcherVo dispatcherVo);

}
