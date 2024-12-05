package neatlogic.framework.process.constvalue;

import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.operationauth.core.OperationAuthHandlerType;

import java.util.Arrays;
import java.util.List;

public enum ProcessTaskOperationType implements IOperationType {

    PROCESSTASK_VIEW("pocesstaskview", "工单查看"),//有服务上报权限才能查看对应工单信息
    PROCESSTASK_START("startprocess", "上报"),
    PROCESSTASK_WORK("work", "处理"),//可处理步骤权限，有accept、start、complete、startprocess、completetask其中一个权限，就会有work权限
    /**
     * 取消
     * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_ABORT("abortprocessTask", "取消", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.OWNER.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    /**
     * 恢复
     * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_RECOVER("recoverprocessTask", "恢复"),
    /**
     * 转交
     * 只有该节点处于‘待处理’或‘进行中’状态时，才允许转交；无论是查看页面还是处理页面，处理人还是非处理人，只要用户有权限转交，便能看到该按钮，否则按钮不显示；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_TRANSFER("transfer", "转交", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    PROCESSTASK_UPDATE("update", "修改上报内容", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),//包括标题、优先级、描述
    PROCESSTASK_URGE("urge", "催办", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.OWNER.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    PROCESSTASK_RETREAT("retreat", "撤回", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),

    PROCESSTASK_REDO("redo", "回退"),
    PROCESSTASK_SCORE("score", "评分"),
    PROCESSTASK_SHOW("show", "取消隐藏"),
    PROCESSTASK_HIDE("hide", "隐藏"),
    PROCESSTASK_DELETE("delete", "删除"),
    PROCESSTASK_TRANSFERREPORT("tranferreport", "转报"),
    PROCESSTASK_MARKREPEAT("markrepeat", "标记重复"),
    PROCESSTASK_COPYPROCESSTASK("copyprocesstask", "复制上报"),
    PROCESSTASK_FOCUSUSER_UPDATE("updatefocususer", "修改工单关注人"),
    PROCESSTASK_REACTIVATE("reactivate", "重新激活"),
    TRANSFER_EOA_STEP("transfereoastep", "转交审批步骤"),
    ;
    private String status;
    private String text;
    /**
     * 节点管理中默认授权对象
     **/
    private List<String> defaultValue;
    /**
     * 节点管理中可授权对象类型
     **/
    private List<String> groupList;

    private ProcessTaskOperationType(String _status, String _text) {
        this.status = _status;
        this.text = _text;
    }

    private ProcessTaskOperationType(String _status, String _text, List<String> _defaultValue, List<String> _groupList) {
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
        return text;
    }

    @Override
    public OperationAuthHandlerType getOperationAuthHandlerType() {
        return OperationAuthHandlerType.TASK;
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
