package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class WORKCENTER_ADVANCED_SEARCH extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "工单中心-高级搜索权限";
	}

	@Override
	public String getAuthIntroduction() {
		return "工单中心开放高级搜索";
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
