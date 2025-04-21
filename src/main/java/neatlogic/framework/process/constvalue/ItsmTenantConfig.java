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

import neatlogic.framework.common.constvalue.ApiParamType;
import neatlogic.framework.config.ITenantConfig;
import neatlogic.framework.util.$;

public enum ItsmTenantConfig implements ITenantConfig {
    PROCESS_TASK_BASE_INFO_IS_SHOW("processtaskBaseInfoIsShow", null, "term.itsm.isshowbaseinfo", ApiParamType.INTEGER),
    PROCESS_TASK_STEP_LIST_IS_SHOW("processtask.step.list.show", "1", "nfpc.itsmtenantconfig.steplist.show", ApiParamType.INTEGER),
    PROCESS_TASK_STEP_ENABLE_COMMENT("processTaskStepEnableComment", null, "nfpc.itsmtenantconfig.processtaskstepenablecomment", ApiParamType.INTEGER),
//    DISPLAY_MODE_AFTER_TIMEOUT("displayModeAfterTimeout", "naturalTime", "nfpc.itsmtenantconfig.displaymodeaftertimeout"),
    SLA_TIME_DISPLAY_MODE("sla.time.display.mode", "naturalTime", "nfpc.itsmtenantconfig.slatimedisplaymode", ApiParamType.STRING),
    PROCESS_TASK_STEP_COMMENT_EDITOR_TOOLBAR_IS_SHOW("processTaskStepCommentEditorToolbarIsShow", "1", "term.itsm.isshowprocesstaskstepcommenteditortoolbar", ApiParamType.INTEGER),
    WORKCENTER_AUTO_REFRESH("workcenter.auto.refresh", "1", "nfpc.itsmtenantconfig.workcenterrefresh", ApiParamType.INTEGER),
    WORKCENTER_CUSTOM_LIMIT("workcenter.custom.limit", "5", "nfpc.itsmtenantconfig.workcentercustomlimit", ApiParamType.INTEGER),
    WORKCENTER_PROCESSTASK_NEWPAGE("workcenter.processtask.newpage", "0", "nfpc.itsmtenantconfig.workcenterprocesstasknewpage", ApiParamType.INTEGER),
    PROCESSTASK_TAB_LAYOUT("processtask.tab.layout", "{}", "nfpc.itsmtenantconfig.processtasktablayout", ApiParamType.JSONOBJECT),

    PROCESSTASK_WORKERPOLICY_ISONLYONCEEXECUTE("processtask.workerpolicy.isonlyonceexecute", "0", "nfpc.itsmtenantconfig.processtaskworkerpolicyisonlyonceexecute", ApiParamType.INTEGER),
    PROCESSTASK_STEP_AUTOAPPROVAL_SHOW("processtask.step.autoapproval.show", "0", "nfpc.itsmtenantconfig.processtaskstepautoapprovalshow", ApiParamType.INTEGER),
    ;

    String key;
    String value;
    String description;
    ApiParamType type;

    ItsmTenantConfig(String key, String value, String description, ApiParamType type) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.type = type;
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

    @Override
    public ApiParamType getType() {
        return type;
    }
}
