/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto;

import codedriver.framework.common.constvalue.ApiParamType;
import codedriver.framework.restful.annotation.EntityField;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * @author linbq
 * @since 2021/8/20 11:52
 **/
public class AssignableWorkerStepVo {
    @EntityField(name = "工单步骤id", type = ApiParamType.LONG)
    private Long id;
    @EntityField(name = "步骤名称", type = ApiParamType.STRING)
    private String name;
    @EntityField(name = "步骤uuid", type = ApiParamType.STRING)
    private String processStepUuid;
    @EntityField(name = "回复是否必填", type = ApiParamType.INTEGER)
    private Integer isRequired;
    @EntityField(name = "范围列表", type = ApiParamType.JSONARRAY)
    private JSONArray rangeList;
    @EntityField(name = "目标列表", type = ApiParamType.JSONARRAY)
    private JSONArray groupList;
    @EntityField(name = "流转到下列步骤时才需要指定处理人", type = ApiParamType.JSONARRAY)
    private List<Long> nextStepIdList;
    @EntityField(name = "流转到下列步骤时才需要指定处理人", type = ApiParamType.JSONARRAY)
    private List<String> nextStepUuidList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessStepUuid() {
        return processStepUuid;
    }

    public void setProcessStepUuid(String processStepUuid) {
        this.processStepUuid = processStepUuid;
    }

    public Integer getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Integer isRequired) {
        this.isRequired = isRequired;
    }

    public JSONArray getRangeList() {
        return rangeList;
    }

    public void setRangeList(JSONArray rangeList) {
        this.rangeList = rangeList;
    }

    public JSONArray getGroupList() {
        return groupList;
    }

    public void setGroupList(JSONArray groupList) {
        this.groupList = groupList;
    }

    public List<Long> getNextStepIdList() {
        return nextStepIdList;
    }

    public void setNextStepIdList(List<Long> nextStepIdList) {
        this.nextStepIdList = nextStepIdList;
    }

    public List<String> getNextStepUuidList() {
        return nextStepUuidList;
    }

    public void setNextStepUuidList(List<String> nextStepUuidList) {
        this.nextStepUuidList = nextStepUuidList;
    }
}
