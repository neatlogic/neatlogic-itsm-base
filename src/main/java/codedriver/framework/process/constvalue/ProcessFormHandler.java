package codedriver.framework.process.constvalue;

import java.util.List;

import codedriver.framework.common.constvalue.ParamType;
import codedriver.framework.common.constvalue.Expression;
import codedriver.framework.common.constvalue.FormHandlerType;

public enum ProcessFormHandler implements IProcessFormHandler {

    FORMSELECT("formselect", "下拉框", "ts-sitemap", "form", FormHandlerType.SELECT, ParamType.ARRAY,
        List.class.getSimpleName().toLowerCase()),
    FORMINPUT("forminput", "文本框", "ts-textmodule", "form", FormHandlerType.INPUT, ParamType.STRING,
        String.class.getSimpleName().toLowerCase()),
    FORMTEXTAREA("formtextarea", "文本域", "ts-text", "form", FormHandlerType.TEXTAREA, ParamType.STRING,
        String.class.getSimpleName().toLowerCase()),
    FORMEDITOR("formeditor", "富文本框", "ts-viewmodule", "form", FormHandlerType.EDITOR, ParamType.STRING,
        String.class.getSimpleName().toLowerCase()),
    FORMRADIO("formradio", "单选框", "ts-complete", "form", FormHandlerType.RADIO, ParamType.ARRAY,
        String.class.getSimpleName().toLowerCase()),
    FORMCHECKBOX("formcheckbox", "复选框", "ts-check-square-o", "form", FormHandlerType.CHECKBOX, ParamType.ARRAY,
        List.class.getSimpleName().toLowerCase()),
    FORMDATE("formdate", "日期", "ts-calendar", "form", FormHandlerType.DATE, ParamType.DATE,
        String.class.getSimpleName().toLowerCase()),
    FORMTIME("formtime", "时间", "ts-timer", "form", FormHandlerType.TIME, ParamType.DATE,
        String.class.getSimpleName().toLowerCase()),
    FORMSTATICLIST("formstaticlist", "静态列表", "ts-list", "form", null, null, null),
    FORMCASCADELIST("formcascadelist", "级联下拉", "ts-formlist", "form", FormHandlerType.CASCADELIST, ParamType.STRING,
        String.class.getSimpleName().toLowerCase()),
    FORMDYNAMICLIST("formdynamiclist", "动态列表", "ts-viewlist", "form", null, null, null),
    FORMDIVIDER("formdivider", "分割线", "ts-minus", "form", null, null, null),
    FORMUSERSELECT("formuserselect", "用户选择器", "ts-user", "form", FormHandlerType.USERSELECT, ParamType.ARRAY,
        String.class.getSimpleName().toLowerCase()),
    FORMLINK("formlink", "链接", "ts-link", "form", null, ParamType.STRING, String.class.getSimpleName().toLowerCase());

    private String handler;
    private String handlerName;
    private FormHandlerType handlerType;
    private ParamType paramType;
    private String dataType;
    private String icon;
    private String type;

    private ProcessFormHandler(String _handler, String _handlerName, String _icon, String _type,
        FormHandlerType _handlerType, ParamType _paramType, String _dataType) {
        this.handler = _handler;
        this.handlerName = _handlerName;
        this.icon = _icon;
        this.type = _type;
        this.handlerType = _handlerType;
        this.paramType = _paramType;
        this.dataType = _dataType;
    }

    public static String getHandlerName(String _handler) {
        for (ProcessFormHandler s : ProcessFormHandler.values()) {
            if (s.getHandler().equals(_handler)) {
                return s.getHandlerName();
            }
        }
        return null;
    }

    public static FormHandlerType getHandlerType(String _handler, String processWorkcenterConditionType) {
        for (ProcessFormHandler s : ProcessFormHandler.values()) {
            if (s.getHandler().equals(_handler)) {

                return s.getHandlerType(processWorkcenterConditionType);
            }
        }
        return null;
    }

    public static List<Expression> getExpressionList(String _handler) {
        for (ProcessFormHandler s : ProcessFormHandler.values()) {
            if (s.getHandler().equals(_handler)) {
                return s.getExpressionList();
            }
        }
        return null;
    }

    public FormHandlerType getHandlerType(String processWorkcenterConditionType) {
        if (ProcessConditionModel.CUSTOM.getValue().equals(processWorkcenterConditionType)) {
            if (handlerType == FormHandlerType.RADIO || handlerType == FormHandlerType.CHECKBOX) {
                return FormHandlerType.SELECT;
            }
            if (handlerType == FormHandlerType.TEXTAREA || handlerType == FormHandlerType.EDITOR) {
                return FormHandlerType.INPUT;
            }
        }
        return handlerType;
    }

    public static Expression getExpression(String _handler) {
        for (ProcessFormHandler s : ProcessFormHandler.values()) {
            if (s.getHandler().equals(_handler)) {
                return s.getExpression();
            }
        }
        return null;
    }

    public static String getDataType(String _handler) {
        for (ProcessFormHandler s : ProcessFormHandler.values()) {
            if (s.getHandler().equals(_handler)) {
                return s.getDataType();
            }
        }
        return null;
    }

    public List<Expression> getExpressionList() {
        return this.paramType.getExpressionList();
    }

    public String getHandler() {
        return handler;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public String getDataType() {
        return dataType;
    }

    public Expression getExpression() {
        return this.paramType.getDefaultExpression();
    }

    public ParamType getParamType() {
        return paramType;
    }

    public static ParamType getParamType(String _handler) {
        for (ProcessFormHandler s : ProcessFormHandler.values()) {
            if (s.getHandler().equals(_handler)) {
                return s.getParamType();
            }
        }
        return null;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    public String getType() {
        return type;
    }

    @Override
    public FormHandlerType getHandlerType() {
        return handlerType;
    }

}
