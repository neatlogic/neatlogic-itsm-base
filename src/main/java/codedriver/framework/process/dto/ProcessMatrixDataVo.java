package codedriver.framework.process.dto;

import codedriver.framework.common.dto.BasePageVo;

import java.util.List;

/**
 * @program: codedriver
 * @description:
 * @create: 2020-03-30 16:41
 **/
public class ProcessMatrixDataVo extends BasePageVo {
    private String keyword;
    private String matrixUuid;
    private List<String> columnList;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getMatrixUuid() {
        return matrixUuid;
    }

    public void setMatrixUuid(String matrixUuid) {
        this.matrixUuid = matrixUuid;
    }

    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }
}
