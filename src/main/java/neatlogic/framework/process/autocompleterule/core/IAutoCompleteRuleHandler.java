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

package neatlogic.framework.process.autocompleterule.core;

import neatlogic.framework.process.dto.ProcessTaskStepVo;

/**
 * @author linbq
 * @since 2021/10/29 15:47
 **/
public interface IAutoCompleteRuleHandler {

    String getHandler();

    String getName();

    /**
     *  优先级不能相同
     * @return
     */
    int getPriority();

    boolean execute(ProcessTaskStepVo currentProcessTaskStepVo);
}
