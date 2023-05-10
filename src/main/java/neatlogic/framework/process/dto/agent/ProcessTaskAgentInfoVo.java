/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

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
