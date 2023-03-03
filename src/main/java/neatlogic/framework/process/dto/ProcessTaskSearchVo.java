/*
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

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
    private String excludeStatus;

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
}
