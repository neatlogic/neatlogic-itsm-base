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

package neatlogic.framework.process.workerdispatcher.core;

import java.util.Objects;

/**
 * @author lvzk
 * @since 2021/8/19 11:30
 **/
public enum WorkerDispatcherForm {
    TEXT("text", "文本框组件"),
    USER_SELECT("userselect", "选择用户组件"),
    FORM_SELECT("formselect", "选择表单组件"),
    SELECT("select", "下拉框组件");
    private final String value;
    private final String text;

    private WorkerDispatcherForm(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public static WorkerDispatcherForm getParamMode(String _value) {
        for (WorkerDispatcherForm e : values()) {
            if (Objects.equals(e.getValue(), _value)) {
                return e;
            }
        }
        return null;
    }
}
