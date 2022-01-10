package codedriver.framework.process.workcenter.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.util.CommonUtil;
import codedriver.framework.dashboard.dto.DashboardConfigVo;
import codedriver.framework.dto.AuthorityVo;
import codedriver.framework.dto.condition.ConditionConfigVo;
import codedriver.framework.restful.annotation.EntityField;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

public class WorkcenterVo extends ConditionConfigVo implements Serializable {
    private static final long serialVersionUID = 1952066708451908924L;

    @EntityField(name = "工单中心分类uuid", type = ApiParamType.STRING)
    private String uuid;
    @EntityField(name = "工单中心分类名", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "custom类型,工单中心分类所属人", type = ApiParamType.ENUM)
    private String owner;
    @EntityField(name = "default:默认出厂  system：系统分类  custom：自定义分类", type = ApiParamType.ENUM)
    private String type;
    @JSONField(serialize = false)
    @EntityField(name = "排序", type = ApiParamType.INTEGER)
    private Integer sort;
    @EntityField(name = "数量", type = ApiParamType.INTEGER)
    private String count;
    @EntityField(name = "过滤条件", type = ApiParamType.STRING)
    private String conditionConfig;
    @EntityField(name = "all:所有设备,mobile:手机端,pc:电脑端", type = ApiParamType.ENUM)
    private String support;
    @JSONField(serialize = false)
    @EntityField(name = "显示的字段", type = ApiParamType.JSONARRAY)
    private JSONArray headerList;
    @JSONField(serialize = false)
    private List<AuthorityVo> authorityList;
    @EntityField(name = "角色列表", type = ApiParamType.JSONARRAY)
    private List<String> valueList;
    @EntityField(name = "是否拥有编辑权限", type = ApiParamType.INTEGER)
    private Integer isCanEdit;
    @EntityField(name = "是否拥有授权权限", type = ApiParamType.INTEGER)
    private Integer isCanRole;
    @EntityField(name = "是否附加待我处理条件", type = ApiParamType.INTEGER)
    private Integer isProcessingOfMine = 0;
    @EntityField(name = "待我处理的数量", type = ApiParamType.STRING)
    private String processingOfMineCount;
    @JSONField(serialize = false)
    @EntityField(name = "设备类型", type = ApiParamType.STRING)
    private String device;
    @EntityField(name = "排序的字段", type = ApiParamType.JSONARRAY)
    private JSONArray sortList;
    @EntityField(name = "上报时间条件", type = ApiParamType.JSONOBJECT)
    private JSONObject startTimeCondition;
    @EntityField(name = "关键字搜索条件", type = ApiParamType.JSONOBJECT)
    private JSONArray keywordConditionList;
    @EntityField(name = "菜单类型id", type = ApiParamType.LONG)
    private Long catalogId;
    @EntityField(name = "菜单类型名称", type = ApiParamType.STRING)
    private String catalogName;



    //params
    private List<String> channelUuidList;
    private JSONArray resultColumnList;

    private List<WorkcenterTheadVo> theadVoList;

    private List<Long> processTaskIdList;

    private String sqlFieldType;

    //关键字全文检索字段名（工单号、工单标题、工单内容）
    private String keywordHandler;

    private String keywordPro;

    private String keywordColumn;

    private String keywordText;

    //用于dashboard 搜索入参
    private DashboardConfigVo dashboardConfigVo ;

    public WorkcenterVo() {
    }

    public WorkcenterVo(String _name) {
        this.name = _name;
    }

    public WorkcenterVo(JSONObject jsonObj) {
        super(jsonObj.getJSONObject("conditionConfig"));
        JSONObject conditionConfig = jsonObj.getJSONObject("conditionConfig");
        super.setKeyword(jsonObj.getString("keyword"));
        this.isProcessingOfMine = conditionConfig.getInteger("isProcessingOfMine") != null ? conditionConfig.getInteger("isProcessingOfMine") : 0;
        this.sortList = jsonObj.getJSONArray("sortList");
        this.uuid = jsonObj.getString("uuid");
        this.setCurrentPage(jsonObj.getInteger("currentPage"));
        this.setPageSize(jsonObj.getInteger("pageSize"));
        this.resultColumnList = conditionConfig.getJSONArray("resultColumnList");
        JSONArray conditionGroupArray = conditionConfig.getJSONArray("conditionGroupList");
        if (CollectionUtils.isNotEmpty(conditionGroupArray)) {
            channelUuidList = new ArrayList<String>();
            for (Object conditionGroup : conditionGroupArray) {
                JSONObject conditionGroupJson = (JSONObject) JSONObject.toJSON(conditionGroup);
                JSONArray channelArray = conditionGroupJson.getJSONArray("channelUuidList");
                List<String> channelUuidListTmp = new ArrayList<String>();
                if (CollectionUtils.isNotEmpty(channelArray)) {
                    channelUuidListTmp = JSONObject.parseArray(channelArray.toJSONString(), String.class);
                }
                channelUuidList.addAll(channelUuidListTmp);
            }
        }
        //上报时间过滤条件
        if(conditionConfig.containsKey("startTimeCondition")) {
            startTimeCondition = conditionConfig.getJSONObject("startTimeCondition");
        }else{
            startTimeCondition = JSONObject.parseObject("{\"timeRange\":\"1\",\"timeUnit\":\"year\"}");//默认展示一年
        }

        this.keywordConditionList = conditionConfig.getJSONArray("keywordConditionList");
    }

    public String getUuid() {
        if (StringUtils.isBlank(uuid)) {
            uuid = UUID.randomUUID().toString().replace("-", "");
        }
        return uuid;
    }

    public void setUuid(String uuid) {
        if (StringUtils.isNotBlank(uuid)) {
            this.uuid = uuid;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AuthorityVo> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<AuthorityVo> authorityList) {
        this.authorityList = authorityList;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getConditionConfig() {
        return conditionConfig;
    }

    public void setConditionConfig(String conditionConfig) {
        this.conditionConfig = conditionConfig;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public JSONArray getHeaderList() {
        return headerList;
    }

    public void setHeaderList(JSONArray headerArray) {
        this.headerList = headerArray;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<String> getValueList() {
        if (valueList == null) {
            valueList = new ArrayList<String>();
            for (AuthorityVo workcenterAuthorityVo : this.authorityList) {
                if (workcenterAuthorityVo.getType().equals(GroupSearch.ROLE.getValue())) {
                    valueList.add(GroupSearch.ROLE.getValuePlugin() + workcenterAuthorityVo.getUuid());
                } else if (workcenterAuthorityVo.getType().equals(GroupSearch.USER.getValue())) {
                    valueList.add(GroupSearch.USER.getValuePlugin() + workcenterAuthorityVo.getUuid());
                }
                if (workcenterAuthorityVo.getType().equals(GroupSearch.COMMON.getValue())) {
                    valueList.add(GroupSearch.COMMON.getValuePlugin() + workcenterAuthorityVo.getUuid());
                }
            }
        }
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        this.valueList = valueList;
    }

    public Integer getIsCanEdit() {
        return isCanEdit;
    }

    public void setIsCanEdit(Integer isCanEdit) {
        this.isCanEdit = isCanEdit;
    }

    public Integer getIsCanRole() {
        return isCanRole;
    }

    public void setIsCanRole(Integer isCanRole) {
        this.isCanRole = isCanRole;
    }

    public List<String> getChannelUuidList() {
        return channelUuidList;
    }

    public void setChannelUuidList(List<String> channelUuidList) {
        this.channelUuidList = channelUuidList;
    }

    public Integer getIsProcessingOfMine() {
        return isProcessingOfMine;
    }

    public void setIsProcessingOfMine(Integer isProcessingOfMine) {
        this.isProcessingOfMine = isProcessingOfMine;
    }

    public String getProcessingOfMineCount() {
        return processingOfMineCount;
    }

    public void setProcessingOfMineCount(String processingOfMineCount) {
        this.processingOfMineCount = processingOfMineCount;
    }

    public JSONArray getResultColumnList() {
        return resultColumnList;
    }

    public void setResultColumnList(JSONArray resultColumnList) {
        this.resultColumnList = resultColumnList;
    }

    public String getDevice() {
        if (device == null) {
            device = CommonUtil.getDevice();
        }
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public JSONArray getSortList() {
        return sortList;
    }

    public void setSortList(JSONArray sortList) {
        this.sortList = sortList;
    }

    public String getSqlFieldType() {
        return sqlFieldType;
    }

    public void setSqlFieldType(String sqlFieldType) {
        this.sqlFieldType = sqlFieldType;
    }

    public List<Long> getProcessTaskIdList() {
        return processTaskIdList;
    }

    public void setProcessTaskIdList(List<Long> processTaskIdList) {
        this.processTaskIdList = processTaskIdList;
    }

    public List<WorkcenterTheadVo> getTheadVoList() {
        return theadVoList;
    }

    public void setTheadVoList(List<WorkcenterTheadVo> theadVoList) {
        this.theadVoList = theadVoList;
    }

    public JSONObject getStartTimeCondition() {
        return startTimeCondition;
    }

    public void setStartTimeCondition(JSONObject startTimeCondition) {
        this.startTimeCondition = startTimeCondition;
    }

    public String getKeywordHandler() {
        return keywordHandler;
    }

    public void setKeywordHandler(String keywordHandler) {
        this.keywordHandler = keywordHandler;
    }

    public String getKeywordPro() {
        return keywordPro;
    }

    public void setKeywordPro(String keywordPro) {
        this.keywordPro = keywordPro;
    }

    public String getKeywordColumn() {
        return keywordColumn;
    }

    public void setKeywordColumn(String keywordColumn) {
        this.keywordColumn = keywordColumn;
    }

    public String getKeywordText() {
        return keywordText;
    }

    public void setKeywordText(String keywordText) {
        this.keywordText = keywordText;
    }

    public JSONArray getKeywordConditionList() {
        return keywordConditionList;
    }

    public void setKeywordConditionList(JSONArray keywordConditionList) {
        this.keywordConditionList = keywordConditionList;
    }

    public DashboardConfigVo getDashboardConfigVo() {
        if(dashboardConfigVo == null){
            dashboardConfigVo = new DashboardConfigVo();
        }
        return dashboardConfigVo;
    }

    public void setDashboardConfigVo(DashboardConfigVo dashboardConfigVo) {
        this.dashboardConfigVo = dashboardConfigVo;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }
}
