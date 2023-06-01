package neatlogic.framework.process.constvalue;

import neatlogic.framework.process.stephandler.core.IProcessStepHandlerType;
import neatlogic.framework.util.I18n;

public enum ProcessStepHandlerType implements IProcessStepHandlerType {
    START("start", "start", new I18n("开始")),
    OMNIPOTENT("omnipotent", "process", new I18n("通用节点")),
    END("end", "end", new I18n("结束")),
    CONDITION("condition", "converge", new I18n("条件")),
    DISTRIBUTARY("distributary", "converge", new I18n("分流")),
    OCTOPUS("octopus", "process", new I18n("自动化")),
    AUTOMATIC("automatic", "process", new I18n("自动处理")),
    TIMER("timer", "process", new I18n("定时节点"))
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
