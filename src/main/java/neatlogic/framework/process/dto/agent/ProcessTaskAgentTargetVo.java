/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.dto.agent;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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
