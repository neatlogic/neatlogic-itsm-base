package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stephandler.core.IProcessStepHandlerType;
import neatlogic.framework.util.I18n;

public enum ProcessStepHandlerType implements IProcessStepHandlerType {
    START("start", "start", new I18n("common.start")),
    OMNIPOTENT("omnipotent", "process", new I18n("enum.process.processstephandlertype.omnipotent")),
    END("end", "end", new I18n("common.end")),
    CONDITION("condition", "converge", new I18n("common.condition")),
    DISTRIBUTARY("distributary", "converge", new I18n("enum.process.processstephandlertype.distributary")),
    OCTOPUS("octopus", "process", new I18n("common.autoexec")),
    AUTOMATIC("automatic", "process", new I18n("common.automaticprocessing")),
    TIMER("timer", "process", new I18n("enum.process.processstephandlertype.timer"))
    ;

    private String handler;
    private I18n name;
    private String type;

    private ProcessStepHandlerType(String _handler, String _type, I18n _name) {
        this.handler = _handler;
        this.type = _type;
        this.name = _name;
    }

    public String getHandler() {
        return handler;
    }

    public String getName() {
        return name.toString();
    }

    public String getType() {
        return type;
    }

}
