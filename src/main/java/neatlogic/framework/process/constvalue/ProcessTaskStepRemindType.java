package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stepremind.core.IProcessTaskStepRemindType;

import neatlogic.framework.util.$;
public enum ProcessTaskStepRemindType implements IProcessTaskStepRemindType {

    BACK("back", "nfpc.processtaskstepremindtype.text.back", "nfpc.processtaskstepremindtype.description.back"),
    REDO("redo", "nfpc.processtaskstepremindtype.text.redo", "nfpc.processtaskstepremindtype.description.redo"),
    TRANSFER("transfer", "nfpc.processtaskstepremindtype.text.transfer", ""),
    ERROR("error", "nfpc.processtaskstepremindtype.text.error", ""),
    AUTOMATIC_ERROR("automaticerror", "nfpc.processtaskstepremindtype.text.automatic_error", "");
    private String value;
    private String text;
    private String title;

    ProcessTaskStepRemindType(String value, String text, String title) {
        this.value = value;
        this.text = text;
        this.title = title;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return $.t(text);
    }

    @Override
    public String getTitle() {
        return $.t(title);
    }


}
