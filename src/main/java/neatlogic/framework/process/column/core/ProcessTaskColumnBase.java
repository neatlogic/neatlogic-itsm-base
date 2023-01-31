package neatlogic.framework.process.column.core;


import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.dashboard.dto.DashboardWidgetAllGroupDefineVo;
import neatlogic.framework.process.dto.ProcessTaskVo;
import neatlogic.framework.process.workcenter.dto.JoinTableColumnVo;
import neatlogic.framework.process.workcenter.table.ISqlTable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ProcessTaskColumnBase implements IDashBoardProcessTaskColumn, IProcessTaskColumn {
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
    public void getDashboardAllGroupDefine(DashboardWidgetAllGroupDefineVo dashboardWidgetAllGroupDefineVo, List<Map<String, Object>> dbDataMapList) {
        getMyDashboardAllGroupDefine(dashboardWidgetAllGroupDefineVo, dbDataMapList);
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

    /**
     * //如果存在一级分组二级过滤，如：多值图，则补充count为0的group,以及text
     *
     * @param dashboardWidgetAllGroupDefineVo group声明参
     * @param dbDataMapList                   db数据Map
     * @param groupSqlProName                 对应group的sql as Name
     * @param groupSqlTextName                对应group的sql as Text
     */
    protected void getNoExistGroup(DashboardWidgetAllGroupDefineVo dashboardWidgetAllGroupDefineVo, List<Map<String, Object>> dbDataMapList, String groupSqlProName, String groupSqlTextName) {
        //如果存在一级分组二级过滤，如：多值图，则补充count为0的group,以及text
        if (CollectionUtils.isNotEmpty(dashboardWidgetAllGroupDefineVo.getChartConfigVo().getConfigList())) {
            List<String> statusList = dashboardWidgetAllGroupDefineVo.getChartConfigVo().getConfigList().toJavaList(String.class);
            List<String> noExistGroupNameList = statusList.stream().filter(o -> dbDataMapList.stream().noneMatch(d -> Objects.equals(d.get(groupSqlProName), GroupSearch.removePrefix(o)))).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(noExistGroupNameList)) {
                Map<String, String> groupNameTextMap = getGroupNameTextMap(noExistGroupNameList);
                for (String noExistGroupName : noExistGroupNameList) {
                    if (groupNameTextMap.containsKey(noExistGroupName)) {
                        Map<String, Object> groupDataMap = new HashMap<>();
                        groupDataMap.put(groupSqlProName, noExistGroupName);
                        groupDataMap.put("count", 0);
                        groupDataMap.put(groupSqlTextName, groupNameTextMap.get(noExistGroupName));
                        dbDataMapList.add(groupDataMap);
                    }
                }
            }
        }
    }

    /**
     * 如果存在一级分组二级过滤，如：多值图，则补充count为0的group
     *
     * @param dashboardWidgetAllGroupDefineVo group声明参
     * @param dbDataMapList                   db数据Map
     * @param groupSqlProName                 对应group的sql as Name
     */
    protected void getNoExistGroup(DashboardWidgetAllGroupDefineVo dashboardWidgetAllGroupDefineVo, List<Map<String, Object>> dbDataMapList, String groupSqlProName) {
        if (CollectionUtils.isNotEmpty(dashboardWidgetAllGroupDefineVo.getChartConfigVo().getConfigList())) {
            List<String> statusList = dashboardWidgetAllGroupDefineVo.getChartConfigVo().getConfigList().toJavaList(String.class);
            List<String> noExistStatusList = statusList.stream().filter(o -> dbDataMapList.stream().noneMatch(d -> Objects.equals(d.get(groupSqlProName), o))).collect(Collectors.toList());
            for (String noExistStatus : noExistStatusList) {
                Map<String, Object> statusDataMap = new HashMap<>();
                statusDataMap.put(groupSqlProName, noExistStatus);
                statusDataMap.put("count", "0");
                dbDataMapList.add(statusDataMap);
            }
        }
    }

    @Override
    public Map<String, String> getGroupNameTextMap(List<String> groupNameList) {
        return new HashMap<>();
    }

}
