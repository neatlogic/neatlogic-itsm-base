package neatlogic.framework.process.constvalue;

import neatlogic.framework.common.constvalue.IUserProfileOperate;
import neatlogic.framework.util.$;

public enum UserProfileOperate implements IUserProfileOperate {
    KEEP_ON_CREATE_TASK("keeponcreatetask", "nfpc.userprofileoperate.keeponcreatetask"),
    VIEW_PROCESSTASK_DETAIL("viewprocesstaskdetail", "nfpc.userprofileoperate.viewprocesstaskdetail"),
    BACK_CATALOG_LIST("backcataloglist", "nfpc.userprofileoperate.backcataloglist");

    private String value;
    private String text;

    UserProfileOperate(String _value, String _text) {
        this.value = _value;
        this.text = _text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return $.t(text);
    }

}
