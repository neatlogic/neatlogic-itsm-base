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

package neatlogic.framework.process.dto;

import neatlogic.framework.common.dto.BasePageVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/9/15 14:26
 **/
public class ProcessTaskSearchVo extends BasePageVo {
    private List<String> includeChannelUuidList;
    private List<Long> excludeIdList;
    private Long excludeId;
    private String excludeStatus;
    private String owner;

    public void setIncludeChannelUuid(String includeChannelUuid) {
        if (includeChannelUuidList == null) {
            includeChannelUuidList = new ArrayList<>();
        }
        includeChannelUuidList.add(includeChannelUuid);
    }

    public List<String> getIncludeChannelUuidList() {
        return includeChannelUuidList;
    }

    public void setIncludeChannelUuidList(List<String> includeChannelUuidList) {
        this.includeChannelUuidList = includeChannelUuidList;
    }

    public List<Long> getExcludeIdList() {
        return excludeIdList;
    }

    public void setExcludeIdList(List<Long> excludeIdList) {
        this.excludeIdList = excludeIdList;
    }

    public String getExcludeStatus() {
        return excludeStatus;
    }

    public void setExcludeStatus(String excludeStatus) {
        this.excludeStatus = excludeStatus;
    }

    public Long getExcludeId() {
        return excludeId;
    }

    public void setExcludeId(Long excludeId) {
        this.excludeId = excludeId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
