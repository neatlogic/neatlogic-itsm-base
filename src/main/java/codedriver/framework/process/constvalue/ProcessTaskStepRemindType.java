package codedriver.framework.process.constvalue;

import codedriver.framework.process.stepremind.core.IProcessTaskStepRemindType;

public enum ProcessTaskStepRemindType implements IProcessTaskStepRemindType{

    BACK("back", "回退提醒", "回退了【processTaskStepName】，原因"),
    REDO("redo", "回退提醒", "回退了工单，原因"),
    TRANSFER("transfer", "转交提醒", "转交【processTaskStepName】，原因"),
    ERROR("error", "异常提醒", "【processTaskStepName】步骤发生异常，原因");
    private String value;
    private String text;
    private String title;
    
    private ProcessTaskStepRemindType(String value, String text, String title) {
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
        return text;
    }
    @Override
    public String getTitle() {
        return title;
    }
    
    
}
