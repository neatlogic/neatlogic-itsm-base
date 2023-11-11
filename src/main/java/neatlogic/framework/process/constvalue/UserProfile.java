package neatlogic.framework.process.constvalue;

import neatlogic.framework.common.constvalue.IUserProfile;
import neatlogic.framework.common.constvalue.IUserProfileOperate;
import neatlogic.framework.util.$;

import java.util.Arrays;
import java.util.List;

public enum UserProfile implements IUserProfile {
    PROCESSTASK_SUCCESS("processtasksuccess", "nfpc.userprofile.processtasksuccess", Arrays.asList(UserProfileOperate.KEEP_ON_CREATE_TASK, UserProfileOperate.VIEW_PROCESSTASK_DETAIL, UserProfileOperate.BACK_CATALOG_LIST));

    private final String value;
    private final String text;
    private final List<IUserProfileOperate> userProfileOperateList;

    private UserProfile(String _value, String _text, List<IUserProfileOperate> _userProfileOperateList) {
        this.value = _value;
        this.text = _text;
        this.userProfileOperateList = _userProfileOperateList;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return $.t(text);
    }

    @Override
    public List<IUserProfileOperate> getProfileOperateList() {
        return userProfileOperateList;
    }

    public static String getText(String value) {
        for (UserProfile f : UserProfile.values()) {
            if (f.getValue().equals(value)) {
                return f.getText();
            }
        }
        return "";
    }

    @Override
    public String getModuleId() {
        return "process";
    }

}
