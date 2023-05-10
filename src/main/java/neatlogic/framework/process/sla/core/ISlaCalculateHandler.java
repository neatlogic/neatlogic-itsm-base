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

package neatlogic.framework.process.sla.core;

import neatlogic.framework.process.constvalue.SlaStatus;
import neatlogic.framework.process.constvalue.SlaType;
import neatlogic.framework.process.dto.ProcessTaskSlaTimeCostVo;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
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

    /**
     * 名称
     * @return
     */
    String getName();

    /**
     * 类型
     * @return
     */
    SlaType getType();

    /**
     * 描述
     * @return
     */
    String getDescription();
    /**
     * 控制关联步骤下拉框单选或多选
     * @return
     */
    boolean getMultiple();

    /**
     * 如果关联了多个步骤，是否是多个步骤共用一个时效
     * @return
     */
    int isSum();
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
