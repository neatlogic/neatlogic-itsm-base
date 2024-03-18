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
