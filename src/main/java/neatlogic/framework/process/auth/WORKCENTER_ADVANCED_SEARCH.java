package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class WORKCENTER_ADVANCED_SEARCH extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "auth.process.workcenteradvancedsearch.name";
	}

	@Override
	public String getAuthIntroduction() {
		return "auth.process.workcenteradvancedsearch.introduction";
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
