package neatlogic.framework.process.constvalue;

import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.operationauth.core.OperationAuthHandlerType;

import java.util.Arrays;
import java.util.List;

import neatlogic.framework.util.$;
public enum ProcessTaskOperationType implements IOperationType {

    PROCESSTASK_VIEW("pocesstaskview", "nfpc.processtaskoperationtype.text.processtask_view"),//有服务上报权限才能查看对应工单信息
    PROCESSTASK_START("startprocess", "nfpc.processtaskoperationtype.text.processtask_start"),
    PROCESSTASK_WORK("work", "nfpc.processtaskoperationtype.text.processtask_work"),//可处理步骤权限，有accept、start、complete、startprocess、completetask其中一个权限，就会有work权限
    /**
     * 取消
     * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_ABORT("abortprocessTask", "nfpc.processtaskoperationtype.text.processtask_abort", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.OWNER.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    /**
     * 恢复
     * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_RECOVER("recoverprocessTask", "nfpc.processtaskoperationtype.text.processtask_recover"),
    /**
     * 转交
     * 只有该节点处于‘待处理’或‘进行中’状态时，才允许转交；无论是查看页面还是处理页面，处理人还是非处理人，只要用户有权限转交，便能看到该按钮，否则按钮不显示；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    PROCESSTASK_TRANSFER("transfer", "nfpc.processtaskoperationtype.text.processtask_transfer", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    PROCESSTASK_UPDATE("update", "nfpc.processtaskoperationtype.text.processtask_update", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),//包括标题、优先级、描述
    PROCESSTASK_URGE("urge", "nfpc.processtaskoperationtype.text.processtask_urge", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.OWNER.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    PROCESSTASK_RETREAT("retreat", "nfpc.processtaskoperationtype.text.processtask_retreat", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),

    PROCESSTASK_REDO("redo", "nfpc.processtaskoperationtype.text.processtask_redo"),
    PROCESSTASK_SCORE("score", "nfpc.processtaskoperationtype.text.processtask_score"),
    PROCESSTASK_SHOW("show", "nfpc.processtaskoperationtype.text.processtask_show"),
    PROCESSTASK_HIDE("hide", "nfpc.processtaskoperationtype.text.processtask_hide"),
    PROCESSTASK_DELETE("delete", "nfpc.processtaskoperationtype.text.processtask_delete"),
    PROCESSTASK_TRANSFERREPORT("tranferreport", "nfpc.processtaskoperationtype.text.processtask_transferreport"),
    PROCESSTASK_MARKREPEAT("markrepeat", "nfpc.processtaskoperationtype.text.processtask_markrepeat"),
    PROCESSTASK_COPYPROCESSTASK("copyprocesstask", "nfpc.processtaskoperationtype.text.processtask_copyprocesstask"),
    PROCESSTASK_FOCUSUSER_UPDATE("updatefocususer", "nfpc.processtaskoperationtype.text.processtask_focususer_update"),
    PROCESSTASK_REACTIVATE("reactivate", "nfpc.processtaskoperationtype.text.processtask_reactivate"),
    TRANSFER_EOA_STEP("transfereoastep", "nfpc.processtaskoperationtype.text.transfer_eoa_step"),
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
        return $.t(text);
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
