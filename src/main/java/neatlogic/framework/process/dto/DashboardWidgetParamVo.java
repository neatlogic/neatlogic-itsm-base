/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package neatlogic.framework.process.dto;

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.dashboard.dto.DashboardWidgetChartConfigVo;
import neatlogic.framework.restful.annotation.EntityField;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.LinkedHashMap;

public class DashboardWidgetParamVo extends SqlDecoratorVo implements Serializable {
    @EntityField(name = "数据源handler", type = ApiParamType.STRING)
    private String dataSourceHandler;
    @EntityField(name = "chart配置", type = ApiParamType.STRING)
    private DashboardWidgetChartConfigVo dashboardWidgetChartConfigVo;
    @EntityField(name = "上报时间条件", type = ApiParamType.JSONOBJECT)
    private JSONObject startTimeCondition;
    @EntityField(name = "一级分组搜索结果转换后的map，用于二级分组过滤 ", type = ApiParamType.STRING)
    private LinkedHashMap<String, Object> dbExchangeGroupDataMap;

    private static final long serialVersionUID = 5224373347379360928L;

    public DashboardWidgetParamVo() {
    }

    @Override
    public void init() {
        //什么都不需要做
    }

    public DashboardWidgetParamVo(JSONObject conditionConfig, Integer currentPage, DashboardWidgetChartConfigVo dashboardWidgetChartConfigVo, String dataSourceHandler) {
        super(conditionConfig);
        this.setPageSize(currentPage);
        //上报时间过滤条件
        if (conditionConfig.containsKey("startTimeCondition")) {
            startTimeCondition = conditionConfig.getJSONObject("startTimeCondition");
        } else {
            startTimeCondition = JSONObject.parseObject("{\"timeRange\":\"1\",\"timeUnit\":\"year\"}");//默认展示一年
        }
        this.dataSourceHandler = dataSourceHandler;
        this.dashboardWidgetChartConfigVo = dashboardWidgetChartConfigVo;
    }

    public String getDataSourceHandler() {
        return dataSourceHandler;
    }

    public void setDataSourceHandler(String dataSourceHandler) {
        this.dataSourceHandler = dataSourceHandler;
    }

    public DashboardWidgetChartConfigVo getDashboardWidgetChartConfigVo() {
        return dashboardWidgetChartConfigVo;
    }

    public void setDashboardWidgetChartConfigVo(DashboardWidgetChartConfigVo dashboardWidgetChartConfigVo) {
        this.dashboardWidgetChartConfigVo = dashboardWidgetChartConfigVo;
    }

    public JSONObject getStartTimeCondition() {
        return startTimeCondition;
    }

    public void setStartTimeCondition(JSONObject startTimeCondition) {
        this.startTimeCondition = startTimeCondition;
    }

    public LinkedHashMap<String, Object> getDbExchangeGroupDataMap() {
        return dbExchangeGroupDataMap;
    }

    public void setDbExchangeGroupDataMap(LinkedHashMap<String, Object> dbExchangeGroupDataMap) {
        this.dbExchangeGroupDataMap = dbExchangeGroupDataMap;
    }
}
