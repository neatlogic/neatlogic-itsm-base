package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

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

}
