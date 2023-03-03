/*
Copyright(c) $today.year NeatLogic Co., Ltd. All Rights Reserved.

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

package neatlogic.framework.process.dto;

/**
 * @author linbq
 * @since 2021/9/13 16:55
 **/
public class ProcessTaskRepeatVo {
    private Long processTaskId;
    private Long repeatGroupId;

    public ProcessTaskRepeatVo() {
    }

    public ProcessTaskRepeatVo(Long processTaskId, Long repeatGroupId) {
        this.processTaskId = processTaskId;
        this.repeatGroupId = repeatGroupId;
    }

    public Long getProcessTaskId() {
        return processTaskId;
    }

    public void setProcessTaskId(Long processTaskId) {
        this.processTaskId = processTaskId;
    }

    public Long getRepeatGroupId() {
        return repeatGroupId;
    }

    public void setRepeatGroupId(Long repeatGroupId) {
        this.repeatGroupId = repeatGroupId;
    }
}
