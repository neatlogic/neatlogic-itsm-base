package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class PRIORITY_MODIFY extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "优先级管理权限";
	}

	@Override
	public String getAuthIntroduction() {
		return "对优先级添加、修改和删除";
	}

	@Override
	public String getAuthGroup() {
		return "process";
	}

	@Override
	public Integer getSort() {
		return 6;
	}

	@Override
	public List<Class<? extends AuthBase>> getIncludeAuths(){
		return Collections.singletonList(PROCESS_BASE.class);
	}

}
