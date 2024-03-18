package neatlogic.framework.process.constvalue;

public enum ProcessTaskStepTaskSource implements IProcessTaskSource {
    PROCESSTASKSTEP("工单步骤", "processtaskstep");
    private final String text;
    private final String value;


    ProcessTaskStepTaskSource(String _text, String _value) {
        this.text = _text;
        this.value = _value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getText() {
        return text;
    }
}
