/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.sla.core;

import codedriver.framework.process.dto.ProcessTaskSlaTimeCostVo;
import org.springframework.util.ClassUtils;

/**
 * @author linbq
 * @since 2021/11/22 12:05
 **/
public interface ISlaCalculateHandler {
    default String getHandler() {
        return ClassUtils.getUserClass(this.getClass()).getSimpleName();
    }
    String getName();
    String getDescription();

    /**
     * 根据processtask_step_timeaudit表数据计算出时效关联的步骤已经消耗的时长
     * @param slaId 时效id
     * @param currentTimeMillis 当前时间毫秒数
     * @param worktimeUuid 工作时间uuid
     * @return
     */
    ProcessTaskSlaTimeCostVo calculateTimeCost(Long slaId, long currentTimeMillis, String worktimeUuid);
}
