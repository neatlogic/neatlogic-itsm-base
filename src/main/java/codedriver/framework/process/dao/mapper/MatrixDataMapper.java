package codedriver.framework.process.dao.mapper;

import codedriver.framework.process.dto.ProcessMatrixColumnVo;
import codedriver.framework.process.dto.ProcessMatrixDataVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MatrixDataMapper {
    public int insertDynamicTableData(@Param("columnList") List<String> columnList, @Param("dataList") List<String> dataList, @Param("matrixUuid") String matrixUuid);

	public int insertDynamicTableData2(@Param("rowData") List<ProcessMatrixColumnVo> rowData, @Param("matrixUuid") String matrixUuid);

	public int updateDynamicTableDataByUuid(@Param("rowData") List<ProcessMatrixColumnVo> rowData, @Param("uuidColumn") ProcessMatrixColumnVo uuidColumn, @Param("matrixUuid") String matrixUuid);

    public int deleteDynamicTableDataByUuid(@Param("matrixUuid") String matrixUuid, @Param("uuid") String uuid);

    public int getDynamicTableDataCount(ProcessMatrixDataVo dataVo);

    public int getDynamicTableDataCountByUuid(@Param("uuid") String uuid,@Param("matrixUuid") String matrixUuid);

    public List<Map<String, String>> searchDynamicTableData(ProcessMatrixDataVo dataVo);

    public List<Map<String, String>> getDynamicTableDataByColumnList(ProcessMatrixDataVo dataVo);
    
    //public List<Map<String, String>> getDynamicTableDataByDataUuidList(@Param("dataUuidList") List<String> dataUuidList, @Param("targetColumnList") List<String> targetColumnList, @Param("matrixUuid") String matrixUuid);
    public List<Map<String, String>> getDynamicTableDataByUuidList(ProcessMatrixDataVo processMatrixDataVo);

	public int getDynamicTableDataByColumnCount(ProcessMatrixDataVo dataVo);
	
	public int getDynamicTableDataByUuidCount(ProcessMatrixDataVo dataVo);
	
	public String getDynamicTableCellData(@Param("matrixUuid") String matrixUuid, @Param("sourceColumnVo") ProcessMatrixColumnVo sourceColumnVo, @Param("targetColumn") String targetColumn);
}
