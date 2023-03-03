package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class WORKCENTER_NEW_TYPE extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "auth.process.workcenternewtype.name";
	}

	@Override
	public String getAuthIntroduction() {
		return "auth.process.workcenternewtype.introduction";
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
