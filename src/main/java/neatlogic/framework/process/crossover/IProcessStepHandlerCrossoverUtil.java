/*
 * Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.process.audithandler.core.IProcessTaskAuditType;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.ProcessTaskVo;
import neatlogic.framework.process.stepremind.core.IProcessTaskStepRemindType;

public interface IProcessStepHandlerCrossoverUtil extends ICrossoverService {
    /**
     * @Description: 触发动作
     * @Author: linbq
     * @Date: 2021/1/20 16:15
     * @Params:[currentProcessTaskStepVo, trigger]
     * @Returns:void
     **/
    void action(ProcessTaskStepVo currentProcessTaskStepVo, INotifyTriggerType trigger);

    /**
     * @Description: 触发通知
     * @Author: linbq
     * @Date: 2021/1/20 16:17
     * @Params:[currentProcessTaskStepVo, trigger]
     * @Returns:void
     **/
    void notify(ProcessTaskStepVo currentProcessTaskStepVo, INotifyTriggerType trigger);

    /**
     * @Description: 计算时效
     * @Author: linbq
     * @Date: 2021/1/20 16:17
     * @Params:[currentProcessTaskVo, isAsync]
     * @Returns:void
     **/
    void calculateSla(ProcessTaskVo currentProcessTaskVo);

    /**
     * @Description: 计算时效
     * @Author: linbq
     * @Date: 2021/1/20 16:17
     * @Params:[currentProcessTaskVo, isAsync]
     * @Returns:void
     **/
    void calculateSla(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * @Description: 记录操作时间
     * @Author: linbq
     * @Date: 2021/1/20 16:19
     * @Params:[currentProcessTaskStepVo, action]
     * @Returns:void
     **/
    void timeAudit(ProcessTaskStepVo currentProcessTaskStepVo, IOperationType action);

    /**
     * @Description: 记录操作活动
     * @Author: linbq
     * @Date: 2021/1/20 16:19
     * @Params:[currentProcessTaskStepVo, action]
     * @Returns:void
     **/
    void audit(ProcessTaskStepVo currentProcessTaskStepVo, IProcessTaskAuditType action);

    /**
     * @Description: 获取验证基本信息数据是否合法，并验证
     * @Author: linbq
     * @Date: 2021/1/20 16:21
     * @Params:[currentProcessTaskStepVo]
     * @Returns:boolean
     **/
    boolean baseInfoValidFromDb(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * @Description: 验证前置步骤指派处理人是否合法
     * @Author: linbq
     * @Date: 2021/1/20 16:21
     * @Params:[currentProcessTaskStepVo]
     * @Returns:boolean
     **/
    boolean assignWorkerValid(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * @Description: 保存步骤提醒
     * @Author: linbq
     * @Date: 2021/1/21 11:30
     * @Params:[currentProcessTaskStepVo, targerProcessTaskStepId, reason, ation]
     * @Returns:int
     **/
    int saveStepRemind(ProcessTaskStepVo currentProcessTaskStepVo, Long targerProcessTaskStepId, String reason, IProcessTaskStepRemindType ation);

    /**
     * @Description: 保存描述内容和附件
     * @Author: linbq
     * @Date: 2021/1/27 11:41
     * @Params:[currentProcessTaskStepVo, action]
     * @Returns:void
     **/
    void saveContentAndFile(ProcessTaskStepVo currentProcessTaskStepVo, IOperationType action);

    /**
     * 保存工单级别的操作描述内容
     * @param currentProcessTaskVo
     * @param action
     */
    void saveProcessTaskOperationContent(ProcessTaskVo currentProcessTaskVo, IOperationType action);

    /**
     * 流转操作时检查回复信息是否必填
     * @param currentProcessTaskStepVo
     */
    void checkContentIsRequired(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * @Description: 保存标签列表
     * @Author: linbq
     * @Date: 2021/1/27 11:42
     * @Params:[currentProcessTaskStepVo]
     * @Returns:void
     **/
    void saveTagList(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * @Description: 保存工单关注人
     * @Author: laiwt
     * @Date: 2021/2/19 11:20
     * @Params: [currentProcessTaskStepVo]
     * @Returns: void
     **/
    void saveFocusUserList(ProcessTaskStepVo currentProcessTaskStepVo);

    /**
     * 保存表单属性值
     * @param currentProcessTaskStepVo 当前步骤信息
     */
    void saveForm(ProcessTaskStepVo currentProcessTaskStepVo);
}
