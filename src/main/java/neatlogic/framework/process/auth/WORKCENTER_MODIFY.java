package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Arrays;
import java.util.List;

public class WORKCENTER_MODIFY extends AuthBase {

    @Override
    public String getAuthDisplayName() {
        return "auth.process.workcentermodify.name";
    }

    @Override
    public String getAuthIntroduction() {
        return "auth.process.workcentermodify.introduction";
    }

    @Override
    public String getAuthGroup() {
        return "process";
    }

    @Override
    public Integer getSort() {
        return 13;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths() {
        return Arrays.asList(PROCESS_BASE.class, WORKCENTER_ADVANCED_SEARCH.class, WORKCENTER_NEW_TYPE.class);
    }

}
