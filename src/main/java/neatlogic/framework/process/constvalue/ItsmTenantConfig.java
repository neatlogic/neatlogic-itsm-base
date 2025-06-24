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
import org.apache.commons.lang3.StringUtils;

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
    PROCESSTASK_TAB_LAYOUT("processtask.tab.layout", "{}", "nfpc.itsmtenantconfig.processtasktablayout", ApiParamType.JSONOBJECT, "{\n" +
            "\t\"position\": \"above/below\",\n" +
            "\t\"layoutList\": [\n" +
            "\t\t{\n" +
            "\t\t\t\"key\": \"report\",\n" +
            "\t\t\t\"top\": false,\n" +
            "\t\t\t\"description\": \"上报内容\"\n" +
            "\t\t}\n" +
            "\t],\n" +
            "\t\"description\": \"position字段用于设置固定位置，值为above时固定到上方，值为below时固定到下方；layoutList集合中元素的key和top是必填字段，description是选填字段；key是tab的唯一标识，top字段用于设置是否默认固定，值为true时默认固定；layoutList集合中元素的顺序就是固定位置顺序，从上到下，从左到右，在layoutList集合显式设置的tab会排在前面；key字段的取值：report(上报内容),collection(工单集合),dataconversion(数据转换),eoa(电子签批),subProcess(子流程),autoexec(自动化),automatic(自动处理),changecreate(变更详情),changehandle(变更详情),cmdbsync(配置项同步),createjob(创建作业),diagram(架构设计),taskConfigList(子任务策略),preNode(前置步骤节点信息(eoa)),step(步骤日志),activity(时间线),relevance(关联工单),markrepeat(重复事件),file(附件清单),reportingHistory(上报历史)\"\n" +
            "}"),

    PROCESSTASK_WORKERPOLICY_ISONLYONCEEXECUTE("processtask.workerpolicy.isonlyonceexecute", "0", "nfpc.itsmtenantconfig.processtaskworkerpolicyisonlyonceexecute", ApiParamType.INTEGER),
    PROCESSTASK_STEP_AUTOAPPROVAL_SHOW("processtask.step.autoapproval.show", "0", "nfpc.itsmtenantconfig.processtaskstepautoapprovalshow", ApiParamType.INTEGER),
    ;

    String key;
    String value;
    String description;
    ApiParamType type;
    String help;

    ItsmTenantConfig(String key, String value, String description, ApiParamType type) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.type = type;
    }

    ItsmTenantConfig(String key, String value, String description, ApiParamType type, String help) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.type = type;
        this.help = help;
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
        if (StringUtils.isNotBlank(this.help)) {
            return $.t(description) + ", " + help;
        } else {
            return $.t(description);
        }
    }

    @Override
    public ApiParamType getType() {
        return type;
    }
}
