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

import neatlogic.framework.util.I18n;

/**
 * 时效类型，如响应时效、处理时效
 *
 * @author linbq
 * @since 2022/2/22 16:27
 **/
public enum SlaType {
    RESPONSE("response", new I18n("enum.process.slatype.response")),
    HANDLE("handle", new I18n("enum.process.slatype.handle"));
    private final String value;
    private final I18n text;

    SlaType(String value, I18n text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text.toString();
    }
}
