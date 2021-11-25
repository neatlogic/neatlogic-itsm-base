/*
 * Copyright(c) 2021 TechSureCo.,Ltd.AllRightsReserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.sla.core;

import codedriver.framework.process.constvalue.SlaStatus;
import codedriver.framework.process.dto.ProcessTaskSlaTimeCostVo;
import codedriver.framework.process.dto.ProcessTaskStepVo;
import org.springframework.util.ClassUtils;

import java.util.List;

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
     * 根据时效关联的步骤状态返回时效的状态
     * @param processTaskStepList 时效关联的步骤列表
     * @return
     */
    SlaStatus getStatus(List<ProcessTaskStepVo> processTaskStepList);

    /**
     * 判断已存在的时效是否需要删除，例如某个处理时效在步骤重新激活时，要重新计算耗时，
     * 在步骤重新激活到开始处理的响应阶段，不应该展示上次处理的时效信息，这时候就要删除上次的时效信息
     * @return
     */
    boolean needDelete(List<ProcessTaskStepVo> processTaskStepList);
    /**
     * 根据processtask_step_timeaudit表数据计算出时效关联的步骤已经消耗的时长
     * @param slaId 时效id
     * @param currentTimeMillis 当前时间毫秒数
     * @param worktimeUuid 工作时间uuid
     * @return
     */
    ProcessTaskSlaTimeCostVo calculateTimeCost(Long slaId, long currentTimeMillis, String worktimeUuid);
}
