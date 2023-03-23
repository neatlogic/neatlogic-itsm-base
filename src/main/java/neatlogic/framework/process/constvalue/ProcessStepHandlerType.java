package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stephandler.core.IProcessStepHandlerType;
import neatlogic.framework.util.I18nUtils;

public enum ProcessStepHandlerType implements IProcessStepHandlerType {
    START("start", "start", "enum.process.processstephandlertype.start"),
    OMNIPOTENT("omnipotent", "process", "enum.process.processstephandlertype.omnipotent"),
    END("end", "end", "enum.process.processstephandlertype.end"),
    CONDITION("condition", "converge", "enum.process.processstephandlertype.condition"),
    DISTRIBUTARY("distributary", "converge", "enum.process.processstephandlertype.distributary"),
    OCTOPUS("octopus", "process", "enum.process.processstephandlertype.octopus"),
    AUTOMATIC("automatic", "process", "enum.process.processstephandlertype.automatic"),
    TIMER("timer", "process", "enum.process.processstephandlertype.timer")
    ;

    private String handler;
    private String name;
    private String type;

    private ProcessStepHandlerType(String _handler, String _type, String _name) {
        this.handler = _handler;
        this.type = _type;
        this.name = _name;
    }

    public String getHandler() {
        return handler;
    }

    public String getName() {
        return I18nUtils.getMessage(name);
    }

    public String getType() {
        return type;
    }

}
