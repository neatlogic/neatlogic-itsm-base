/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
