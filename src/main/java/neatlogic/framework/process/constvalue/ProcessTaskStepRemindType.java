package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stepremind.core.IProcessTaskStepRemindType;
import neatlogic.framework.util.I18n;

public enum ProcessTaskStepRemindType implements IProcessTaskStepRemindType {

    BACK("back", new I18n("回退提醒"), new I18n("回退了【processTaskStepName】，原因")),
    REDO("redo", new I18n("回退提醒"), new I18n("回退了工单，原因")),
    TRANSFER("transfer", new I18n("转交提醒"), new I18n("转交提醒")),
    ERROR("error", new I18n("异常提醒"), new I18n("异常提醒"));
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
