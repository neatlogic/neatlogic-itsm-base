package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

public class PROCESS extends AuthBase {
    @Override
    public String getAuthDisplayName() {
        return "nfpa.process.getauthdisplayname";
    }

    @Override
    public String getAuthIntroduction() {
        return "nfpa.process.getauthintroduction";
    }

    @Override
    public String getAuthGroup() {
        return "process";
    }

    @Override
    public Integer getSort() {
        return 14;
    }

    @Override
    public boolean isShow() {
        return false;
    }
}
