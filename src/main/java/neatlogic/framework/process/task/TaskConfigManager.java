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

package neatlogic.framework.process.task;

import neatlogic.framework.process.constvalue.ProcessTaskStepTaskUserStatus;
import neatlogic.framework.process.constvalue.task.TaskConfigPolicy;
import neatlogic.framework.process.dto.ProcessTaskStepTaskVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author lvzk
 * @since 2021/9/22 11:17
 **/
@Component
public class TaskConfigManager {
    private final Map<String, Action<ProcessTaskStepTaskVo>> configMap = new HashMap<>();

    public Map<String, Action<ProcessTaskStepTaskVo>> getConfigMap() {
        return configMap;
    }

    @PostConstruct
    public void actionDispatcherInit() {
        configMap.put(TaskConfigPolicy.ALL.getValue(), (processTaskStepTaskVo) -> {
            boolean result = true;
            if (CollectionUtils.isNotEmpty(processTaskStepTaskVo.getStepTaskUserVoList())) {
                if(processTaskStepTaskVo.getStepTaskUserVoList().stream().anyMatch(t->!Objects.equals(t.getStatus(), ProcessTaskStepTaskUserStatus.SUCCEED.getValue()))){
                    result = false;
                }
            }
            return result;
        });

        configMap.put(TaskConfigPolicy.ANY.getValue(), (processTaskStepVo) -> {
            boolean result = false;
            if (CollectionUtils.isNotEmpty(processTaskStepVo.getStepTaskUserVoList())) {
                if(processTaskStepVo.getStepTaskUserVoList().stream().anyMatch(t->Objects.equals(t.getStatus(), ProcessTaskStepTaskUserStatus.SUCCEED.getValue()))){
                    result = true;
                }
            }
            return result;
        });
    }


    @FunctionalInterface
    public interface Action<T> {
        boolean execute(T t);
    }

}
