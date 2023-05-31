package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stepremind.core.IProcessTaskStepRemindType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskStepRemindType implements IProcessTaskStepRemindType {

    BACK("back", new I18n("common.backtips"), new I18n("enum.process.processtaskstepremindtype.back.b")),
    REDO("redo", new I18n("common.backtips"), new I18n("enum.process.processtaskstepremindtype.redo.b")),
    TRANSFER("transfer", new I18n("enum.process.processtaskstepremindtype.transfer.a"), new I18n("enum.process.processtaskstepremindtype.transfer.a")),
    ERROR("error", new I18n("enum.process.processtaskstepremindtype.error.a"), new I18n("enum.process.processtaskstepremindtype.error.a"));
    private String value;
    private I18n text;
    private I18n title;

    private ProcessTaskStepRemindType(String value, I18n text, I18n title) {
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
        return text.toString();
    }

    @Override
    public String getTitle() {
        return title.toString();
    }


}
