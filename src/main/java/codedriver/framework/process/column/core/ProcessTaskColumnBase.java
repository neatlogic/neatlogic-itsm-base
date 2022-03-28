package codedriver.framework.process.column.core;


import codedriver.framework.dashboard.dto.DashboardWidgetAllGroupDefineVo;
import codedriver.framework.process.dto.ProcessTaskVo;
import codedriver.framework.process.workcenter.dto.JoinTableColumnVo;
import codedriver.framework.process.workcenter.table.ISqlTable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ProcessTaskColumnBase implements IProcessTaskColumn {

    @Override
    public Boolean getIsShow() {
        return getMyIsShow();
    }

    public Boolean getMyIsShow() {
        return true;
    }

    @Override
    public Boolean getDisabled() {
        return false;
    }

    @Override
    public Boolean getIsSort() {
        return false;
    }

    @Override
    public String getSimpleValue(ProcessTaskVo taskVo) {
        return null;
    }

    @Override
    public Boolean getIsExport() {
        return getMyIsExport();
    }

    public Boolean getMyIsExport() {
        return true;
    }

    @Override
    public List<JoinTableColumnVo> getJoinTableColumnList() {
        return getMyJoinTableColumnList();
    }

    public List<JoinTableColumnVo> getMyJoinTableColumnList() {
        return new ArrayList<>();
    }

    @Override
    public String getSortSqlColumn(Boolean isColumn) {
        return getMySortSqlColumn(isColumn);
    }

    protected String getMySortSqlColumn(Boolean isColumn) {
        return StringUtils.EMPTY;
    }

    @Override
    public ISqlTable getSortSqlTable() {
        return getMySortSqlTable();
    }

    protected ISqlTable getMySortSqlTable() {
        return null;
    }

    @Override
    public void getDashboardAllGroupDefine(DashboardWidgetAllGroupDefineVo dashboardDataVo, List<Map<String, Object>> dbDataMapList) {
        getMyDashboardAllGroupDefine(dashboardDataVo, dbDataMapList);
    }

    protected void getMyDashboardAllGroupDefine(DashboardWidgetAllGroupDefineVo dashboardDataVo, List<Map<String, Object>> mapList) {

    }

    @Override
    public LinkedHashMap<String, Object> getExchangeToDashboardGroupDataMap(List<Map<String, Object>> mapList) {
        return getMyExchangeToDashboardGroupDataMap(mapList);
    }

    protected LinkedHashMap<String, Object> getMyExchangeToDashboardGroupDataMap(List<Map<String, Object>> mapList) {
        return null;
    }

    ;

}
