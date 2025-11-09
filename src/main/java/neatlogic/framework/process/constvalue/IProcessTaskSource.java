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

package neatlogic.framework.process.constvalue;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.ProcessTaskVo;

/**
 * 工单上报来源或办理渠道
 *
 * @author laiwt
 * @since 2022/7/6 14:17
 **/
public interface IProcessTaskSource {

    String getValue();

    String getText();

    default String getType(){
        return ProcessTaskSourceType.ITSM.getValue();
    }

    /**
     * 保存工单会执行的操作
     *
     * @param paramObj 上报暂存入参
     * @param processTaskVo 工单对象
     */
    default void saveDraft(JSONObject paramObj, ProcessTaskVo processTaskVo){
    }

    /**
     * 上报工单会执行的操作
     *
     * @param processTaskStepVo 工单当前步骤对象
     */
    default void startProcess(ProcessTaskStepVo processTaskStepVo){
    }

    /**
     * 保存工单会执行的操作
     *
     * @param processTaskStepVo 工单步骤对象
     */
    default void complete (ProcessTaskStepVo processTaskStepVo){

    }
}
