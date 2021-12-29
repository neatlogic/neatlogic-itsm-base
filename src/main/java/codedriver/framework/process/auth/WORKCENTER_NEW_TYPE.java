package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class WORKCENTER_NEW_TYPE extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "工单中心-另存为新分类权限";
	}

	@Override
	public String getAuthIntroduction() {
		return "工单中心开放另存为新分类。注意：通过该方式保存的新分类将不受固化条件限制，收回权限则通过该方式保存的分类都将隐藏";
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
