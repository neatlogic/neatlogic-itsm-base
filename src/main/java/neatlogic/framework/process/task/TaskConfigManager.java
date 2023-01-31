/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package neatlogic.framework.process.task;

import neatlogic.framework.process.constvalue.ProcessTaskStatus;
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
                if(processTaskStepTaskVo.getStepTaskUserVoList().stream().anyMatch(t->!Objects.equals(t.getStatus(), ProcessTaskStatus.SUCCEED.getValue()))){
                    result = false;
                }
            }
            return result;
        });

        configMap.put(TaskConfigPolicy.ANY.getValue(), (processTaskStepVo) -> {
            boolean result = false;
            if (CollectionUtils.isNotEmpty(processTaskStepVo.getStepTaskUserVoList())) {
                if(processTaskStepVo.getStepTaskUserVoList().stream().anyMatch(t->Objects.equals(t.getStatus(), ProcessTaskStatus.SUCCEED.getValue()))){
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
