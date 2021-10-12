/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.dto.agent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.xlsx4j.sml.Col;

/**
 * @author linbq
 * @since 2021/10/9 18:33
 **/
public class ProcessTaskAgentTargetVo {
    @JSONField(serialize = false)
    private Long processTaskAgentId;
    private String target;
    private String type;
    private JSONArray pathList;
    @JSONField(serialize = false)
    private String pathListStr;

    public Long getProcessTaskAgentId() {
        return processTaskAgentId;
    }

    public void setProcessTaskAgentId(Long processTaskAgentId) {
        this.processTaskAgentId = processTaskAgentId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONArray getPathList() {
        if (CollectionUtils.isEmpty(pathList) && StringUtils.isNotBlank(pathListStr)) {
            pathList = JSONArray.parseArray(pathListStr);
        }
        return pathList;
    }

    public void setPathList(JSONArray pathList) {
        this.pathList = pathList;
    }

    public String getPathListStr() {
        if (StringUtils.isBlank(pathListStr) && CollectionUtils.isNotEmpty(pathList)) {
            pathListStr = pathList.toJSONString();
        }
        return pathListStr;
    }

    public void setPathListStr(String pathListStr) {
        this.pathListStr = pathListStr;
    }
}
