package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class PROCESS_STEP_HANDLER_MODIFY extends AuthBase{

    @Override
    public String getAuthDisplayName() {
        return "节点管理权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "对节点管理中的组件配置进行修改";
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
