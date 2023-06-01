package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18n;

public enum UserProfileOperate {
    KEEP_ON_CREATE_TASK("keeponcreatetask", new I18n("继续上报")),
    VIEW_PROCESSTASK_DETAIL("viewprocesstaskdetail", new I18n("查看工单详情")),
    BACK_CATALOG_LIST("backcataloglist", new I18n("返回服务目录列表"));

    private String value;
    private I18n text;

    private UserProfileOperate(String _value, I18n _text) {
        this.value = _value;
        this.text = _text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text.toString();
    }

}
