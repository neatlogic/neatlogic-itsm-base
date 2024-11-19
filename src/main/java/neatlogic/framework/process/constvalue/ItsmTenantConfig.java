/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

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

package neatlogic.framework.process.constvalue;

import neatlogic.framework.config.ITenantConfig;
import neatlogic.framework.util.$;

public enum ItsmTenantConfig implements ITenantConfig {
    PROCESS_TASK_BASE_INFO_IS_SHOW("processtaskBaseInfoIsShow", null, "term.itsm.isshowbaseinfo"),
    PROCESS_TASK_STEP_ENABLE_COMMENT("processTaskStepEnableComment", null, "nfpc.itsmtenantconfig.processtaskstepenablecomment"),
//    DISPLAY_MODE_AFTER_TIMEOUT("displayModeAfterTimeout", "naturalTime", "nfpc.itsmtenantconfig.displaymodeaftertimeout"),
    SLA_TIME_DISPLAY_MODE("sla.time.display.mode", "naturalTime", "nfpc.itsmtenantconfig.slatimedisplaymode"),
    PROCESS_TASK_STEP_COMMENT_EDITOR_TOOLBAR_IS_SHOW("processTaskStepCommentEditorToolbarIsShow", "1", "term.itsm.isshowprocesstaskstepcommenteditortoolbar"),
    WORKCENTER_AUTO_REFRESH("workcenter.auto.refresh", "1", "nfpc.itsmtenantconfig.workcenterrefresh"),
    WORKCENTER_CUSTOM_LIMIT("workcenter.custom.limit", "5", "nfpc.itsmtenantconfig.workcentercustomlimit"),
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
