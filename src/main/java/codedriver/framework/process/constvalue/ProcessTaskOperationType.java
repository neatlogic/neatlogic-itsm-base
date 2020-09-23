package codedriver.framework.process.constvalue;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import codedriver.framework.common.constvalue.GroupSearch;
import codedriver.framework.common.constvalue.UserType;

public enum ProcessTaskOperationType {
    /**
     * 查看节点信息
     * 无论该节点的状态如何，只有被授权的对象可以查看活动和步骤中所有与该节点相关的信息，否则不可见；（活动是整个活动不可见，步骤只可见步骤名称和状态，其他内容不可见）
     * 查看节点信息不会在步骤中记录或生成活动；
     */
    VIEW("view", "查看节点信息", Arrays.asList(GroupSearch.COMMON.getValuePlugin() + UserType.ALL.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    POCESSTASKVIEW("pocesstaskview","工单查看"),//有服务上报权限才能查看对应工单信息
    STARTPROCESS("startprocess", "上报"),
    START("start", "开始"),
    ACTIVE("active", "激活"),
    COMPLETE("complete", "流转"),//下一步骤列表中有未激活的步骤时，有流转权限
    RETREAT("retreat", "撤回", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    ACCEPT("accept", "开始"),
    WORK("work","处理"),//可处理步骤权限，有accept、start、complete、startprocess、completesubtask其中一个权限，就会有work权限
    /**
     * 取消
     * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    ABORTPROCESSTASK("abortprocessTask", "取消", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    /**
     * 恢复
     * 该功能是工单级别的操作，但将权限下放到步骤中配置；被授权的对象可在该步骤处于‘待处理’或‘进行中’时取消/恢复工单，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    RECOVERPROCESSTASK("recoverprocessTask", "恢复"),
    /**
     * 转交
     * 只有该节点处于‘待处理’或‘进行中’状态时，才允许转交；无论是查看页面还是处理页面，处理人还是非处理人，只要用户有权限转交，便能看到该按钮，否则按钮不显示；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    TRANSFER("transfer", "转交", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    BACK("back", "回退"),//下一步骤列表中有已激活的步骤时，有回退权限
    SAVE("save", "暂存"),
    /**
     * 修改上报内容
     * 被授权的对象可在该步骤处于‘待处理’或‘进行中’时修改上报内容，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集；
     * 如果是步骤处理人操作，会记录在步骤中，同时生成一条活动；如果是非处理人操作，则步骤中无需记录，只生成活动即可；
     */
    UPDATE("update", "修改上报内容", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),//包括标题、优先级、描述
    COMMENT("comment", "回复"),
    EDITCOMMENT("editcomment", "编辑回复"),
    DELETECOMMENT("deletecomment", "删除回复"),
    CREATESUBTASK("createsubtask", "创建子任务"),
    EDITSUBTASK("editsubtask", "编辑"),
    ABORTSUBTASK("abortsubtask", "取消"),
    REDOSUBTASK("redosubtask", "重做"),
    COMPLETESUBTASK("completesubtask", "完成"),
    /**
     * 只有子任务创建人和处理人有回复子任务权限
     */
    COMMENTSUBTASK("commentsubtask", "评论"),
    /**
     * 催办
     * 被授权的对象可在该步骤处于‘待处理’或‘进行中’时催办，如果同时有多个“待处理”或‘进行中’的步骤，则将每个步骤中配置的授权对象取并集，配置的对象都有权限进行催办操作，无论是查看页面还是处理页面，处理人还是非处理人，有权限的用户就能看到该按钮，否则按钮不显示；
     * 催办时会生成一条活动，并在对应的步骤中记录；如果配置了催办的通知设置，还会根据配置进行通知；
     */
    URGE("urge","催办", Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValuePlugin() + ProcessUserType.MAJOR.getValue()), Arrays.asList(GroupSearch.COMMON.getValue(), ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue(), GroupSearch.USER.getValue(), GroupSearch.TEAM.getValue(), GroupSearch.ROLE.getValue())),
    PAUSE("pause","暂停", new ArrayList<String>(), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue())),
    RECOVER("recover","恢复", new ArrayList<String>(), Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue())),
    RESTFULACTION("restfulaction", "RESTFUL动作"),
    RETREATCURRENTSTEP("retreatcurrentstep", "撤回当前步骤"),
    TRANFERREPORT("tranferreport", "转报"),
    COPYPROCESSTASK("copyprocesstask", "复制上报"),
    REDO("redo", "重做"),
    SCORE("score", "评分");
    private String status;
    private String text;
    /** 节点管理中默认授权对象 **/
    private List<String> acceptList;
    /** 节点管理中可授权对象类型 **/
    private List<String> groupList;
    private ProcessTaskOperationType(String _status, String _text) {
        this.status = _status;
        this.text = _text;
    }

    private ProcessTaskOperationType(String _status, String _text, List<String> _acceptList, List<String> _groupList) {
        this.status = _status;
        this.text = _text;
        this.acceptList = _acceptList;
        this.groupList = _groupList;
    }

    public String getValue() {
        return status;
    }

    public String getText() {
        return text;
    }

    public List<String> getAcceptList() {
        return acceptList;
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
