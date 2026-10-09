/*
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package neatlogic.framework.process.dto;

import neatlogic.framework.common.dto.BasePageVo;

import java.util.List;

public class ProcessTaskStepAuditSearchVo extends BasePageVo {

    private Long processTaskId;
    private List<Long> processTaskStepIdList;
    private List<Long> noViewProcessTaskStepIdList;
    private String sortDirection = "desc";

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public List<Long> getProcessTaskStepIdList() {
        return processTaskStepIdList;
    }

    public void setProcessTaskStepIdList(List<Long> processTaskStepIdList) {
        this.processTaskStepIdList = processTaskStepIdList;
    }

    public List<Long> getNoViewProcessTaskStepIdList() {
        return noViewProcessTaskStepIdList;
    }

    public void setNoViewProcessTaskStepIdList(List<Long> noViewProcessTaskStepIdList) {
        this.noViewProcessTaskStepIdList = noViewProcessTaskStepIdList;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
