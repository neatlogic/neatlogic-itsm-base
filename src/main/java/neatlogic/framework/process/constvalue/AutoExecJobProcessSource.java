/*
 * Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package neatlogic.framework.process.constvalue;

import neatlogic.framework.autoexec.source.IAutoexecJobSource;
import neatlogic.framework.util.I18nUtils;

public enum AutoExecJobProcessSource implements IAutoexecJobSource {
    ITSM("enum.process.autoexecjobprocesssource.itsm", "itsm");
    private final String text;
    private final String value;

    AutoExecJobProcessSource(String _text, String _value) {
        this.text = _text;
        this.value = _value;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return I18nUtils.getMessage(text);
    }

    public static String getText(String _status) {
        for (AutoExecJobProcessSource s : AutoExecJobProcessSource.values()) {
            if (s.getValue().equals(_status)) {
                return s.getText();
            }
        }
        return "";
    }
}
