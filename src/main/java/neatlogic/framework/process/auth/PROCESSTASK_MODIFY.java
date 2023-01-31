package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class PROCESSTASK_MODIFY extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "工单管理权限";
	}

	@Override
	public String getAuthIntroduction() {
		return "对工单隐藏/显示和删除，批量上报";
	}

	@Override
	public String getAuthGroup() {
		return "process";
	}

	@Override
	public Integer getSort() {
		return 11;
	}

	@Override
	public List<Class<? extends AuthBase>> getIncludeAuths(){
		return Collections.singletonList(PROCESS_BASE.class);
	}

}
