package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.process.constvalue.ProcessTaskGroupSearch;
import neatlogic.framework.process.constvalue.ProcessUserType;
import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ProcessTaskStepNotifyTriggerType implements INotifyTriggerType {

    ACTIVE("active", "nfpnc.processtaskstepnotifytriggertype.text.active", "nfpnc.processtaskstepnotifytriggertype.description.active",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MAJOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MINOR.getValue())),

    REACTIVATE("reactivate", "nfpnc.processtaskstepnotifytriggertype.text.reactivate", "nfpnc.processtaskstepnotifytriggertype.description.reactivate",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MAJOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MINOR.getValue())),
    ASSIGNEXCEPTION("assignexception", "nfpnc.processtaskstepnotifytriggertype.text.assignexception", "nfpnc.processtaskstepnotifytriggertype.description.assignexception",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MAJOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MINOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue())),
    START("start", "nfpnc.processtaskstepnotifytriggertype.text.start", "nfpnc.processtaskstepnotifytriggertype.description.start",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MINOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue())),
    TRANSFER("transfer", "nfpnc.processtaskstepnotifytriggertype.text.transfer", "nfpnc.processtaskstepnotifytriggertype.description.transfer",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MAJOR.getValue())),
    SUCCEED("succeed", "nfpnc.processtaskstepnotifytriggertype.text.succeed", "nfpnc.processtaskstepnotifytriggertype.description.succeed",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue())),
    BACK("back", "nfpnc.processtaskstepnotifytriggertype.text.back", "nfpnc.processtaskstepnotifytriggertype.description.back",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue())),
    RETREAT("retreat", "nfpnc.processtaskstepnotifytriggertype.text.retreat", "nfpnc.processtaskstepnotifytriggertype.description.retreat",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MAJOR.getValue())),
    REVOKE("revoke", "nfpnc.processtaskstepnotifytriggertype.text.revoke", "nfpnc.processtaskstepnotifytriggertype.description.revoke",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MAJOR.getValue())),
    PAUSE("pause", "nfpnc.processtaskstepnotifytriggertype.text.pause", "nfpnc.processtaskstepnotifytriggertype.description.pause",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue())),
    RECOVER("recover", "nfpnc.processtaskstepnotifytriggertype.text.recover", "nfpnc.processtaskstepnotifytriggertype.description.recover",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue())),
    FAILED("failed", "nfpnc.processtaskstepnotifytriggertype.text.failed", "nfpnc.processtaskstepnotifytriggertype.description.failed",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue())),
    URGE("urge", "nfpnc.processtaskstepnotifytriggertype.text.urge", "nfpnc.processtaskstepnotifytriggertype.description.urge"),
	COMMENT("comment", "nfpnc.processtaskstepnotifytriggertype.text.comment","nfpnc.processtaskstepnotifytriggertype.description.comment",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue())),
	;

    private String trigger;
    private String text;
    private String description;
    private List<String> excludeList;

    ProcessTaskStepNotifyTriggerType(String _trigger, String _text, String _description) {
        this.trigger = _trigger;
        this.text = _text;
        this.description = _description;
        this.excludeList = new ArrayList<>();
    }

    ProcessTaskStepNotifyTriggerType(String _trigger, String _text, String _description, List<String> _excludeList) {
        this.trigger = _trigger;
        this.text = _text;
        this.description = _description;
        this.excludeList = _excludeList;
    }

    @Override
    public String getTrigger() {
        return trigger;
    }

    @Override
    public String getText() {
        return $.t(text);
    }

    @Override
    public String getDescription() {
        return $.t(description);
    }

    public static String getText(String trigger) {
        for (ProcessTaskStepNotifyTriggerType n : values()) {
            if (n.getTrigger().equals(trigger)) {
                return n.getText();
            }
        }
        return "";
    }

    @Override
    public List<String> getExcludeList() {
        return excludeList;
    }
}
