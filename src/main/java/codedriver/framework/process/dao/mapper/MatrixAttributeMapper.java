package codedriver.framework.process.dao.mapper;

import codedriver.framework.process.dto.ProcessMatrixAttributeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatrixAttributeMapper {
    public void insertMatrixAttribute(ProcessMatrixAttributeVo matrixAttributeVo);

    public List<ProcessMatrixAttributeVo> getMatrixAttributeByMatrixUuid(String matrixUuid);

    public void deleteAttributeByMatrixUuid(String matrixUuid);

    public int checkMatrixAttributeTableExist(String tableName);

    public void createMatrixDynamicTable(@Param("attributeList") List<ProcessMatrixAttributeVo> attributeList,@Param("matrixUuid") String matrixUuid);

    public void dropMatrixDynamicTable(String tableName);

    public void addMatrixDynamicTableColumn(@Param("columnName") String columnName,@Param("matrixUuid") String matrixUuid);

    public void dropMatrixDynamicTableColumn(@Param("columnName") String columnName,@Param("matrixUuid") String matrixUuid);
}
