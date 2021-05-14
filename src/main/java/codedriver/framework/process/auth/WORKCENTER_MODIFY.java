package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

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
	public Integer getSort() {
		return 13;
	}

	@Override
	public List<Class<? extends AuthBase>> getIncludeAuths(){
		return Collections.singletonList(PROCESS_BASE.class);
	}

}
