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

package neatlogic.framework.process.stephandler.core;

import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.operationauth.core.IOperationType;

/**
 * 工单步骤操作后处理器接口
 */
public interface IProcessTaskOperatePostProcessor {

    /**
     * 该方法在步骤操作完成后执行
     * @param currentProcessTaskStepVo 步骤信息
     * @param operationType 操作类型
     */
    default void postProcessAfterProcessTaskStepOperate(ProcessTaskStepVo currentProcessTaskStepVo, IOperationType operationType) {

    }
}
