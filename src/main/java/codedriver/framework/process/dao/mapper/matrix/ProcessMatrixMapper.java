package codedriver.framework.process.dao.mapper.matrix;

import codedriver.framework.matrix.dto.ProcessMatrixFormComponentVo;

public interface ProcessMatrixMapper {

//    public List<ProcessMatrixDispatcherVo> getMatrixDispatcherByMatrixUuid(String matrixUuid);

//	public List<ProcessMatrixFormComponentVo> getMatrixFormComponentByMatrixUuid(String matrixUuid);
	
	public int insertMatrixFormComponent(ProcessMatrixFormComponentVo componentVo);
	    
//	public int insertMatrixDispatcher(ProcessMatrixDispatcherVo dispatcherVo);

}
