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

package neatlogic.framework.process.crossover;

import com.alibaba.fastjson.JSONArray;
import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.dto.ProcessTaskStepTaskVo;
import neatlogic.framework.process.dto.ProcessTaskStepVo;
import neatlogic.framework.process.dto.TaskConfigVo;

import java.util.List;

public interface IProcessTaskStepTaskCrossoverService extends ICrossoverService {
    /**
     * 创建任务
     *
     * @param id
     * @param processTaskStepTaskVo
     * @param stepTaskUserVoList
     * @param source
     */
    Long saveTask(Long id, ProcessTaskStepTaskVo processTaskStepTaskVo, JSONArray stepTaskUserVoList, String source);

    /**
     * 完成任务
     *
     * @param id 任务id
     * @param content 回复内容
     * @param button 按钮
     * @param source 来源
     */
    Long completeTask(Long id, String content, String button, String source) throws Exception;

    /**
     * 获取步骤的任务策略列表及其任务列表
     * @param processTaskStepVo 步骤信息
     * @return
     */
    List<TaskConfigVo> getTaskConfigList(ProcessTaskStepVo processTaskStepVo);
}
