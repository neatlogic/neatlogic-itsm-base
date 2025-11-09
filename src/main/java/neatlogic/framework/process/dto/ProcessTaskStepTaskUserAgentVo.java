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

import java.io.Serializable;

public class ProcessTaskStepTaskUserAgentVo implements Serializable {
    private Long processTaskStepTaskUserId;
    private Long processTaskStepTaskId;
    private String userUuid;
    private String agentUuid;

    public Long getProcessTaskStepTaskUserId() {
        return processTaskStepTaskUserId;
    }

    public void setProcessTaskStepTaskUserId(Long processTaskStepTaskUserId) {
        this.processTaskStepTaskUserId = processTaskStepTaskUserId;
    }

    public Long getProcessTaskStepTaskId() {
        return processTaskStepTaskId;
    }

    public void setProcessTaskStepTaskId(Long processTaskStepTaskId) {
        this.processTaskStepTaskId = processTaskStepTaskId;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getAgentUuid() {
        return agentUuid;
    }

    public void setAgentUuid(String agentUuid) {
        this.agentUuid = agentUuid;
    }
}
