package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class CHANNELTYPE_MODIFY extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "服务类型管理权限";
	}

	@Override
	public String getAuthIntroduction() {
		return "对服务类型添加、修改和删除";
	}

	@Override
	public String getAuthGroup() {
		return "process";
	}

	@Override
	public Integer getSort() {
		return 5;
	}

	@Override
	public List<Class<? extends AuthBase>> getIncludeAuths(){
		return Collections.singletonList(PROCESS_BASE.class);
	}
}
