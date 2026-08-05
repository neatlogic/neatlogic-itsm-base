package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.$;

public enum ProcessTaskStepTaskSource implements IProcessTaskSource {
    PROCESSTASKSTEP("nfpc.processtasksteptasksource.text.processtaskstep", "processtaskstep");
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
        return $.t(text);
    }
}
