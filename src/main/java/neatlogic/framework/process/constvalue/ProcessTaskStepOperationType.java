package neatlogic.framework.process.constvalue;

import neatlogic.framework.common.constvalue.GroupSearch;
import neatlogic.framework.common.constvalue.UserType;
import neatlogic.framework.process.operationauth.core.IOperationType;
import neatlogic.framework.process.operationauth.core.OperationAuthHandlerType;

import java.util.Arrays;
import java.util.List;

import neatlogic.framework.util.$;
public enum ProcessTaskStepOperationType implements IOperationType {

    /**
     * 查看节点信息
     * 无论该节点的状态如何，只有被授权的对象可以查看活动和步骤中所有与该节点相关的信息，否则不可见；（活动是整个活动不可见，步骤只可见步骤名称和状态，其他内容不可见）
     * 查看节点信息不会在步骤中记录或生成活动；
     */
    STEP_VIEW("view", "nfpc.processtaskstepoperationtype.text.step_view", Arrays.asList(GroupSearch.COMMON.getValuePlugin() + UserType.ALL.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_START("start", "nfpc.processtaskstepoperationtype.text.step_start"),
    STEP_ACTIVE("active", "nfpc.processtaskstepoperationtype.text.step_active"),
    STEP_COMPLETE("complete", "nfpc.processtaskstepoperationtype.text.step_complete"),//下一步骤列表中有未激活的步骤时，有流转权限
    STEP_ACCEPT("accept", "nfpc.processtaskstepoperationtype.text.step_accept"),
    STEP_WORK("workcurrentstep", "nfpc.processtaskstepoperationtype.text.step_work"),
    STEP_TRANSFER("transfercurrentstep", "nfpc.processtaskstepoperationtype.text.step_transfer", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_BACK("back", "nfpc.processtaskstepoperationtype.text.step_back"),//下一步骤列表中有已激活的步骤时，有回退权限
    STEP_SAVE("save", "nfpc.processtaskstepoperationtype.text.step_save"),
    STEP_REAPPROVAL("reapproval", "nfpc.processtaskstepoperationtype.text.step_reapproval"),
    STEP_FAIL("fail", "nfpc.processtaskstepoperationtype.text.step_fail"),
    /**
     * 修改上报内容
     * 被授权的对象可在该步骤处于‘待处理’或‘进行中’时修改上报内容，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    STEP_COMMENT("comment", "nfpc.processtaskstepoperationtype.text.step_comment"),
    STEP_EDITCOMMENT("editcomment", "nfpc.processtaskstepoperationtype.text.step_editcomment"),
    STEP_DELETECOMMENT("deletecomment", "nfpc.processtaskstepoperationtype.text.step_deletecomment"),
    /**
     * 催办
     * 被授权的对象可在该步骤处于‘待处理’或‘进行中’时催办，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集，配置的对象都有权限进行催办操作，无论是查看页面还是处理页面，处理人还是非处理人，有权限的用户就能看到该按钮，否则按钮不显示；
     * 催办时会生成一条活动，并在对应的步骤中记录；如果配置了催办的通知设置，还会根据配置进行通知；
     */
    STEP_PAUSE("pause","nfpc.processtaskstepoperationtype.text.step_pause", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_RECOVER("recover", "nfpc.processtaskstepoperationtype.text.step_recover", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_RESTFULACTION("restfulaction", "nfpc.processtaskstepoperationtype.text.step_restfulaction"),
    STEP_RETREAT("retreatcurrentstep",  "nfpc.processtaskstepoperationtype.text.step_retreat", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    STEP_HANG("hang", "nfpc.processtaskstepoperationtype.text.step_hang"),
    STEP_HANDLE("handle", "nfpc.processtaskstepoperationtype.text.step_handle"),
    STEP_REDO("redo", "nfpc.processtaskstepoperationtype.text.step_redo"),
    TASK_CREATE("createtask", "nfpc.processtaskstepoperationtype.text.task_create"),
    TASK_EDIT("edittask", "nfpc.processtaskstepoperationtype.text.task_edit"),
    TASK_DELETE("deletetask", "nfpc.processtaskstepoperationtype.text.task_delete"),
    TASK_COMPLETE("completetask", "nfpc.processtaskstepoperationtype.text.task_complete"),
    TRANSFER_EOA_STEP("transfereoastep", "nfpc.processtaskstepoperationtype.text.transfer_eoa_step"),
    STEP_REACTIVATE("reactivatestep", "nfpc.processtaskstepoperationtype.text.step_reactivate"),
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

    private ProcessTaskStepOperationType(String _status, String _text) {
        this.status = _status;
        this.text = _text;
    }

    private ProcessTaskStepOperationType(String _status, String _text, List<String> _defaultValue, List<String> _groupList) {
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
        return OperationAuthHandlerType.STEP;
    }

    public List<String> getDefaultValue() {
        return defaultValue;
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public static String getValue(String _status) {
        for (ProcessTaskStepOperationType s : ProcessTaskStepOperationType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _status) {
        for (ProcessTaskStepOperationType s : ProcessTaskStepOperationType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getText();
            }
        }
        return "";
    }
}
