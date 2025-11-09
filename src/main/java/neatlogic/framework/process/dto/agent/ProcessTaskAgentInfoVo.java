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

import java.util.Date;
import java.util.List;

/**
 * @author linbq
 * @since 2021/10/9 20:32
 **/
public class ProcessTaskAgentInfoVo {
    private Date beginTime;
    private Date endTime;
    private Integer isActive;
    private List<ProcessTaskAgentCompobVo> compobList;

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public List<ProcessTaskAgentCompobVo> getCompobList() {
        return compobList;
    }

    public void setCompobList(List<ProcessTaskAgentCompobVo> compobList) {
        this.compobList = compobList;
    }
}
