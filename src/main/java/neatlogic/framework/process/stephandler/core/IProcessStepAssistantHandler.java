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
