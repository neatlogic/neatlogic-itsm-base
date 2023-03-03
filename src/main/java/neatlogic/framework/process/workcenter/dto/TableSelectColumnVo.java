package neatlogic.framework.process.workcenter.dto;

import neatlogic.framework.process.workcenter.table.ISqlTable;

import java.util.List;

/**
 * @Title: JoinTableColumnVo
 * @Package: neatlogic.framework.process.workcenter.dto
 * @Description: join Table 关系Vo
 * @Author: 89770
 * @Date: 2021/1/22 16:41
 **/
public class TableSelectColumnVo {
    private ISqlTable table;
    private String tableShortName;
    private List<SelectColumnVo> columnList;

    public TableSelectColumnVo(ISqlTable _table ,List<SelectColumnVo> _columnList) {
        this.table = _table;
        this.tableShortName = table.getShortName();
        this.columnList = _columnList;
    }

    public TableSelectColumnVo(ISqlTable _table , String _tableShortName ,List<SelectColumnVo> _columnList) {
        this.table = _table;
        this.tableShortName = _tableShortName;
        this.columnList = _columnList;
    }

    public ISqlTable getTable() {
        return table;
    }

    public void setTable(ISqlTable table) {
        this.table = table;
    }

    public String getTableShortName() {
        return tableShortName;
    }

    public void setTableShortName(String tableShortName) {
        this.tableShortName = tableShortName;
    }

    public List<SelectColumnVo> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<SelectColumnVo> columnList) {
        this.columnList = columnList;
    }

}
