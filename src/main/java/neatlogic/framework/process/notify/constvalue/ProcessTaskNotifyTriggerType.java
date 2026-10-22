package neatlogic.framework.process.notify.constvalue;

import neatlogic.framework.notify.core.INotifyTriggerType;
import neatlogic.framework.process.constvalue.ProcessTaskGroupSearch;
import neatlogic.framework.process.constvalue.ProcessUserType;
import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ProcessTaskNotifyTriggerType implements INotifyTriggerType {

    STARTPROCESS("startprocess", "nfpnc.processtasknotifytriggertype.text.startprocess", "nfpnc.processtasknotifytriggertype.description.startprocess",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MAJOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MINOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    UPDATEPROCESSTASK("updateprocesstask", "nfpnc.processtasknotifytriggertype.text.updateprocesstask", "nfpnc.processtasknotifytriggertype.description.updateprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    ABORTPROCESSTASK("abortprocesstask", "nfpnc.processtasknotifytriggertype.text.abortprocesstask", "nfpnc.processtasknotifytriggertype.text.abortprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MAJOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.MINOR.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    RECOVERPROCESSTASK("recoverprocesstask", "nfpnc.processtasknotifytriggertype.text.recoverprocesstask", "nfpnc.processtasknotifytriggertype.description.recoverprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    COMPLETEPROCESSTASK("completeprocesstask", "nfpnc.processtasknotifytriggertype.text.completeprocesstask", "nfpnc.processtasknotifytriggertype.description.completeprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    WAITINGSCOREPROCESSTASK("waitingscoreprocesstask", "nfpnc.processtasknotifytriggertype.text.waitingscoreprocesstask", "nfpnc.processtasknotifytriggertype.description.waitingscoreprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    SCOREPROCESSTASK("scoreprocesstask", "nfpnc.processtasknotifytriggertype.text.scoreprocesstask", "nfpnc.processtasknotifytriggertype.text.scoreprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.WORKER.getValue(),
                    ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    REOPENPROCESSTASK("reopenprocesstask", "nfpnc.processtasknotifytriggertype.text.reopenprocesstask", "nfpnc.processtasknotifytriggertype.description.reopenprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    MARKREPEATPROCESSTASK("markrepeatprocesstask", "nfpnc.processtasknotifytriggertype.text.markrepeatprocesstask", "nfpnc.processtasknotifytriggertype.text.markrepeatprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    DELETEPROCESSTASK("deleteprocesstask", "nfpnc.processtasknotifytriggertype.text.deleteprocesstask", "nfpnc.processtasknotifytriggertype.text.deleteprocesstask",
            Arrays.asList(ProcessTaskGroupSearch.PROCESSUSERTYPE.getValue() + "#" + ProcessUserType.DEFAULT_WORKER.getValue())),
    ;

    private String trigger;
    private String text;
    private String description;
    private List<String> excludeList;

    ProcessTaskNotifyTriggerType(String _trigger, String _text, String _description) {
        this.trigger = _trigger;
        this.text = _text;
        this.description = _description;
        this.excludeList = new ArrayList<>();
    }

    ProcessTaskNotifyTriggerType(String _trigger, String _text, String _description, List<String> _excludeList) {
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
        for (ProcessTaskNotifyTriggerType n : values()) {
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
