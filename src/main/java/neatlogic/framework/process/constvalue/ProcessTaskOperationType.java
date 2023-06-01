package neatlogic.framework.process.constvalue;

import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.common.constvalue.UserType;
import neatlogic.framework.util.I18n;

import java.util.Arrays;
import java.util.List;

public enum ProcessTaskOperationType implements IOperationType {

    PROCESSTASK_VIEW("pocesstaskview", new I18n("工单查看")),//有服务上报权限才能查看对应工单信息
    PROCESSTASK_START("startprocess", new I18n("上报")),
    PROCESSTASK_WORK("work", new I18n("处理")),//可处理步骤权限，有accept、start、complete、startprocess、completetask其中一个权限，就会有work权限
    /**
     * 取消
     * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_ABORT("abortprocessTask", new I18n("取消"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.OWNER.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    /**
     * 恢复
     * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_RECOVER("recoverprocessTask", new I18n("恢复")),
    /**
     * 转交
     * 只有该节点处于‘待处理’或‘进行中’状态时，才允许转交；无论是查看页面还是处理页面，处理人还是非处理人，只要用户有权限转交，便能看到该按钮，否则按钮不显示；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_TRANSFER("transfer", new I18n("转交"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    PROCESSTASK_UPDATE("update", new I18n("修改上报内容"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),//包括标题、优先级、描述
    PROCESSTASK_URGE("urge", new I18n("催办"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.OWNER.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    PROCESSTASK_RETREAT("retreat", new I18n("撤回"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),

    PROCESSTASK_REDO("redo", new I18n("回退")),
    PROCESSTASK_SCORE("score", new I18n("评分")),
    PROCESSTASK_SHOW("show", new I18n("取消隐藏")),
    PROCESSTASK_HIDE("hide", new I18n("隐藏")),
    PROCESSTASK_DELETE("delete", new I18n("删除")),
    PROCESSTASK_TRANSFERREPORT("tranferreport", new I18n("转报")),
    PROCESSTASK_MARKREPEAT("markrepeat", new I18n("标记重复")),
    PROCESSTASK_COPYPROCESSTASK("copyprocesstask", new I18n("复制上报")),
    PROCESSTASK_FOCUSUSER_UPDATE("updatefocususer", new I18n("修改工单关注人")),
    /**
     * 查看节点信息
     * 无论该节点的状态如何，只有被授权的对象可以查看活动和步骤中所有与该节点相关的信息，否则不可见；（活动是整个活动不可见，步骤只可见步骤名称和状态，其他内容不可见）
     * 查看节点信息不会在步骤中记录或生成活动；
     */
    STEP_VIEW("view", new I18n("查看节点信息"), Arrays.asList(GroupSearch.COMMON.getValuePlugin() + UserType.ALL.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_START("start", new I18n("开始")),
    STEP_ACTIVE("active", new I18n("激活")),
    STEP_COMPLETE("complete", new I18n("流转")),//下一步骤列表中有未激活的步骤时，有流转权限
    STEP_ACCEPT("accept", new I18n("开始")),
    STEP_WORK("workcurrentstep", new I18n("处理当前步骤")),
    STEP_TRANSFER("transfercurrentstep", new I18n("转交"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_BACK("back", new I18n("回退")),//下一步骤列表中有已激活的步骤时，有回退权限
    STEP_SAVE("save", new I18n("暂存")),
    STEP_REAPPROVAL("reapproval", new I18n("重审")),
    /**
     * 修改上报内容
     * 被授权的对象可在该步骤处于‘待处理’或‘进行中’时修改上报内容，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    STEP_COMMENT("comment", new I18n("回复")),
    STEP_EDITCOMMENT("editcomment", new I18n("编辑回复")),
    STEP_DELETECOMMENT("deletecomment", new I18n("删除回复")),
    /**
     * 催办
     * 被授权的对象可在该步骤处于‘待处理’或‘进行中’时催办，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集，配置的对象都有权限进行催办操作，无论是查看页面还是处理页面，处理人还是非处理人，有权限的用户就能看到该按钮，否则按钮不显示；
     * 催办时会生成一条活动，并在对应的步骤中记录；如果配置了催办的通知设置，还会根据配置进行通知；
     */
    STEP_PAUSE("pause",new I18n("暂停"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_RECOVER("recover", new I18n("恢复"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_RESTFULACTION("restfulaction", new I18n("RESTFUL动作")),
    STEP_RETREAT("retreatcurrentstep",  new I18n("撤回"), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_HANG("hang", new I18n("挂起")),
    STEP_HANDLE("handle", new I18n("自动处理")),
    TASK_CREATE("createtask", new I18n("创建任务")),
    TASK_EDIT("edittask", new I18n("编辑任务")),
    TASK_DELETE("deletetask", new I18n("删除任务")),
    TASK_COMPLETE("completetask", new I18n("完成任务")),
    ;
    private String status;
    private I18n text;
    /**
     * 节点管理中默认授权对象
     **/
    private List<String> defaultValue;
    /**
     * 节点管理中可授权对象类型
     **/
    private List<String> groupList;

    private ProcessTaskOperationType(String _status, I18n _text) {
        this.status = _status;
        this.text = _text;
    }

    private ProcessTaskOperationType(String _status, I18n _text, List<String> _defaultValue, List<String> _groupList) {
        this.status = _status;
        this.text = _text;
        this.defaultValue = _defaultValue;
        this.groupList = _groupList;
    }

    @Override
    public String getValue() {
        return status;
    }

    @Override
    public String getText() {
        return text.toString();
    }

    public List<String> getDefaultValue() {
        return defaultValue;
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public static String getValue(String _status) {
        for (ProcessTaskOperationType s : ProcessTaskOperationType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _status) {
        for (ProcessTaskOperationType s : ProcessTaskOperationType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getText();
            }
        }
        return "";
    }
}
