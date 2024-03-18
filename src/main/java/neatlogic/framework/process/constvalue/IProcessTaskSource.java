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
     * 保存工单会执行的操作
     *
     * @param processTaskStepVo 工单步骤对象
     */
    default void complete (ProcessTaskStepVo processTaskStepVo){

    }
}
