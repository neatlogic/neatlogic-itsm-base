package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stephandler.core.IProcessStepHandlerType;

public enum ProcessStepHandlerType implements IProcessStepHandlerType {
    START("start", "start", "开始"), OMNIPOTENT("omnipotent", "process", "通用节点"), END("end", "end", "结束"),
    CONDITION("condition", "converge", "条件"), DISTRIBUTARY("distributary", "converge", "分流"),
    OCTOPUS("octopus", "process", "自动化"), AUTOMATIC("automatic", "process", "自动处理"), TIMER("timer", "process", "定时节点")
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
        return name;
    }

    public String getType() {
        return type;
    }

}
