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

package neatlogic.framework.process.sla.core;

import neatlogic.framework.process.dao.mapper.ProcessTaskStepTimeAuditMapper;
import neatlogic.framework.process.dto.ProcessTaskSlaTimeCostVo;
import neatlogic.framework.process.dto.ProcessTaskStepTimeAuditVo;
import neatlogic.framework.worktime.dao.mapper.WorktimeMapper;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author linbq
 * @since 2021/11/22 14:46
 **/
public abstract class SlaCalculateHandlerBase implements ISlaCalculateHandler {

    protected static ProcessTaskStepTimeAuditMapper processTaskStepTimeAuditMapper;

    protected static WorktimeMapper worktimeMapper;

    @Resource
    private void setProcessTaskStepTimeAuditMapper(ProcessTaskStepTimeAuditMapper _processTaskStepTimeAuditMapper) {
        processTaskStepTimeAuditMapper = _processTaskStepTimeAuditMapper;
    }

    @Resource
    public void setWorktimeMapper(WorktimeMapper _worktimeMapper) {
        worktimeMapper = _worktimeMapper;
    }

    @Override
    public ProcessTaskSlaTimeCostVo calculateTimeCost(Long slaId, long currentTimeMillis, String worktimeUuid) {
        List<ProcessTaskStepTimeAuditVo> timeAuditList =  processTaskStepTimeAuditMapper.getProcessTaskStepTimeAuditBySlaId(slaId);
        if (CollectionUtils.isEmpty(timeAuditList)) {
            return new ProcessTaskSlaTimeCostVo();
        }
        return myCalculateTimeCost(timeAuditList, currentTimeMillis, worktimeUuid);
    }

    protected abstract ProcessTaskSlaTimeCostVo myCalculateTimeCost(List<ProcessTaskStepTimeAuditVo> timeAuditList, long currentTimeMillis, String worktimeUuid);



    /**
     * 计算出时效关联的步骤已经消耗的时长（直接计算）
     *
     * @param timePeriodList
     * @return
     */
    protected static long getRealTimeCost(List<Map<String, Long>> timePeriodList) {
        long sum = 0;
        for (Map<String, Long> timeMap : timePeriodList) {
            sum += timeMap.get("e") - timeMap.get("s");
        }
        return sum;
    }

    /**
     * 计算出时效关联的步骤已经消耗的时长（根据工作日历计算）
     *
     * @param timePeriodList
     * @param worktimeUuid
     * @return
     */
    protected static long getTimeCost(List<Map<String, Long>> timePeriodList, String worktimeUuid) {
        long sum = 0;
        for (Map<String, Long> timeMap : timePeriodList) {
            sum += worktimeMapper.calculateCostTime(worktimeUuid, timeMap.get("s"), timeMap.get("e"));
        }
        return sum;
    }
}
