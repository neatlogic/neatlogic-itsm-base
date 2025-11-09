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
