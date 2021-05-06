package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

public class WORKCENTER_MODIFY extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "工单中心管理权限";
	}

	@Override
	public String getAuthIntroduction() {
		return "对工单中心系统类型添加、修改和删除";
	}

	@Override
	public String getAuthGroup() {
		return "process";
	}

	@Override
	public Integer sort() {
		return 13;
	}

}
