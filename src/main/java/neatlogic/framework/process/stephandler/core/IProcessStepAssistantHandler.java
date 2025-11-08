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

import neatlogic.framework.process.dto.ProcessTaskStepUserVo;
import neatlogic.framework.process.dto.ProcessTaskStepVo;

import java.util.List;

/**
 * 用于获取步骤任务的协助处理人
 */
public interface IProcessStepAssistantHandler {

    String getHandler();

    /**
     * 获取协助处理人列表，用于通知接收人
     * @param currentProcessTaskStepVo
     * @return
     */
    List<ProcessTaskStepUserVo> getMinorUserListForNotifyReceiver(ProcessTaskStepVo currentProcessTaskStepVo);
}
