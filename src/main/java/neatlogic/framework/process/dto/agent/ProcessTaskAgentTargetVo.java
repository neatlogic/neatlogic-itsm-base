/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

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

package neatlogic.framework.process.dto.agent;

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
