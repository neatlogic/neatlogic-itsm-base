package codedriver.framework.process.column.core;


import codedriver.framework.dashboard.dto.DashboardDataVo;
import codedriver.framework.process.workcenter.dto.JoinTableColumnVo;
import codedriver.framework.process.workcenter.dto.WorkcenterVo;
import codedriver.framework.process.workcenter.table.ISqlTable;
import com.alibaba.fastjson.JSONObject;
import com.techsure.multiattrsearch.MultiAttrsObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class ProcessTaskColumnBase implements IProcessTaskColumn {

    @Override
    public Object getValue(MultiAttrsObject el) throws RuntimeException {
        JSONObject commonJson = (JSONObject) el.getJSON(this.getType());
        if (commonJson == null) {
            return CollectionUtils.EMPTY_COLLECTION;
        }
        return getMyValue(commonJson);
    }

    @Override
    public Object getMyValue(JSONObject json) {
        return null;
    }

    @Override
    public Object getValueText(MultiAttrsObject el) throws RuntimeException {
        JSONObject commonJson = (JSONObject) el.getJSON(this.getType());
        if (commonJson != null) {
            return getMyValueText(commonJson);
        }
        return null;
    }

    public Object getMyValueText(JSONObject json) {
        return null;
    }

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
    public Object getSimpleValue(Object json) {
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
    public String getSortSqlColumn() {
        return getMySortSqlColumn();
    }

    protected String getMySortSqlColumn() {
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
    public void getDashboardDataVo(DashboardDataVo dashboardDataVo, WorkcenterVo workcenterVo, List<Map<String, String>> mapList) {
        getMyDashboardDataVo(dashboardDataVo, workcenterVo, mapList);
    }

    protected void getMyDashboardDataVo(DashboardDataVo dashboardDataVo, WorkcenterVo workcenterVo, List<Map<String, String>> mapList) {

    }

    @Override
    public void getExchangeToDashboardGroupDataMap(List<Map<String, String>> mapList, WorkcenterVo workcenterVo) {
        workcenterVo.setGroupDataCountMap(getMyExchangeToDashboardGroupDataMap(mapList));
    }

    protected LinkedHashMap<String, String> getMyExchangeToDashboardGroupDataMap(List<Map<String, String>> mapList) {
        return null;
    }

    ;

}
