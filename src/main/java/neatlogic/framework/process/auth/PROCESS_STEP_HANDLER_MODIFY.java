package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class PROCESS_STEP_HANDLER_MODIFY extends AuthBase{

    @Override
    public String getAuthDisplayName() {
        return "nfpa.process_step_handler_modify.getauthdisplayname";
    }

    @Override
    public String getAuthIntroduction() {
        return "nfpa.process_step_handler_modify.getauthintroduction";
    }

    @Override
    public String getAuthGroup() {
        return "process";
    }

    @Override
    public Integer getSort() {
        return 9;
    }

    @Override
    public List<Class<? extends AuthBase>> getIncludeAuths(){
        return Collections.singletonList(PROCESS_BASE.class);
    }

}
