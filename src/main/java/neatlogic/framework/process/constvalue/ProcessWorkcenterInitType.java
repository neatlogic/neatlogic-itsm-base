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

package neatlogic.framework.process.constvalue;

/**
 * @author lvzk
 * @since 2021/9/6 11:55
 **/
public enum ProcessWorkcenterInitType {
    ALL_PROCESSTASK("allProcessTask", "所有工单"), DRAFT_PROCESSTASK("draftProcessTask", "我的草稿"), DONE_OF_MINE_PROCESSTASK("doneOfMineProcessTask", "我的已办"), PROCESSING_OF_MINE_PROCESSTASK("processingOfMineProcessTask", "我的待办");
    private final String value;
    private final String name;

    private ProcessWorkcenterInitType(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getValue(String _value) {
        for (ProcessWorkcenterInitType s : ProcessWorkcenterInitType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (ProcessWorkcenterInitType s : ProcessWorkcenterInitType.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }
}
