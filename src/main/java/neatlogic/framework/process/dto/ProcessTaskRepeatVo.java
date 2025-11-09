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

/**
 * @author linbq
 * @since 2021/9/13 16:55
 **/
public class ProcessTaskRepeatVo {
    private Long processTaskId;
    private Long repeatGroupId;

    public ProcessTaskRepeatVo() {
    }

    public ProcessTaskRepeatVo(Long processTaskId, Long repeatGroupId) {
        this.processTaskId = processTaskId;
        this.repeatGroupId = repeatGroupId;
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getRepeatGroupId() {
        return repeatGroupId;
    }

    public void setRepeatGroupId(Long repeatGroupId) {
        this.repeatGroupId = repeatGroupId;
    }
}
