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

import neatlogic.framework.config.ITenantConfig;
import neatlogic.framework.util.$;

public enum ItsmTenantConfig implements ITenantConfig {
    PROCESS_TASK_BASE_INFO_IS_SHOW("processtaskBaseInfoIsShow", null, "term.itsm.isshowbaseinfo"),
    PROCESS_TASK_STEP_ENABLE_COMMENT("processTaskStepEnableComment", null, "nfpc.itsmtenantconfig.processtaskstepenablecomment"),
    DISPLAY_MODE_AFTER_TIMEOUT("displayModeAfterTimeout", "naturalTime", "nfpc.itsmtenantconfig.displaymodeaftertimeout"),
    PROCESS_TASK_STEP_COMMENT_EDITOR_TOOLBAR_IS_SHOW("processTaskStepCommentEditorToolbarIsShow", "1", "term.itsm.isshowprocesstaskstepcommenteditortoolbar"),
    ;

    String key;
    String value;
    String description;

    ItsmTenantConfig(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return $.t(description);
    }
}
