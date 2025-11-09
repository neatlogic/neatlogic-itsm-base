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

import java.util.List;

/**
 * @author lvzk
 * @since 2021/9/9 15:57
 **/
public class ProcessStepTaskConfigVo {
    private String processStepUuid;
    private Long taskConfigId;
    private List<Long> idList;
    private List<String> rangeList;
    public ProcessStepTaskConfigVo(){
        
    }
    public ProcessStepTaskConfigVo(String uuid, Long id) {
        this.taskConfigId = id;
        this.processStepUuid = uuid;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public List<String> getRangeList() {
        return rangeList;
    }

    public void setRangeList(List<String> rangeList) {
        this.rangeList = rangeList;
    }

    public String getProcessStepUuid() {
        return processStepUuid;
    }

    public void setProcessStepUuid(String processStepUuid) {
        this.processStepUuid = processStepUuid;
    }

    public Long getTaskConfigId() {
        return taskConfigId;
    }

    public void setTaskConfigId(Long taskConfigId) {
        this.taskConfigId = taskConfigId;
    }
}
