package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class SCORE_TEMPLATE_MODIFY extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "评分模版管理权限";
	}

	@Override
	public String getAuthIntroduction() {
		return "对评分模版进行添加、修改和删除";
	}

	@Override
	public String getAuthGroup() {
		return "process";
	}

	@Override
	public Integer getSort() {
		return 12;
	}

	@Override
	public List<Class<? extends AuthBase>> getIncludeAuths(){
		return Collections.singletonList(PROCESS_BASE.class);
	}
}
