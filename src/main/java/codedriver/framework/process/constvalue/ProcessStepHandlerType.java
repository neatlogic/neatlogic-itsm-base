package codedriver.framework.process.constvalue;

public enum ProcessStepHandlerType implements IProcessStepHandlerType {
    START("start", "start", "开始"), OMNIPOTENT("omnipotent", "process", "通用节点"), END("end", "end", "结束"),
    CONDITION("condition", "converge", "条件"), DISTRIBUTARY("distributary", "converge", "分流"),
    OCTOPUS("octopus", "process", "自动化"), AUTOMATIC("automatic", "process", "自动处理"),
    CHANGECREATE("changecreate", "process", "变更创建"), CHANGEHANDLE("changehandle", "process", "变更处理"),
    EVENT("event", "process", "事件");

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

    public static String getHandler(String _handler) {
        for (ProcessStepHandlerType s : ProcessStepHandlerType.values()) {
            if (s.getHandler().equals(_handler)) {
                return s.getHandler();
            }
        }
        return null;
    }

    public static String getName(String _handler) {
        for (ProcessStepHandlerType s : ProcessStepHandlerType.values()) {
            if (s.getHandler().equals(_handler)) {
                return s.getName();
            }
        }
        return "";
    }

    public static String getType(String _handler) {
        for (ProcessStepHandlerType s : ProcessStepHandlerType.values()) {
            if (s.getHandler().equals(_handler)) {
                return s.getType();
            }
        }
        return "";
    }

    public String getType() {
        return type;
    }

}
