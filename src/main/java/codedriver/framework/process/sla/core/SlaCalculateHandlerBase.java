/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.sla.core;

import codedriver.framework.process.dao.mapper.ProcessTaskStepTimeAuditMapper;
import codedriver.framework.process.dto.ProcessTaskSlaTimeCostVo;
import codedriver.framework.process.dto.ProcessTaskStepTimeAuditVo;
import codedriver.framework.worktime.dao.mapper.WorktimeMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
