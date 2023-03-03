package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class PROCESSTASK_MODIFY extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "auth.process.processtaskmodify.name";
	}

	@Override
	public String getAuthIntroduction() {
		return "auth.process.processtaskmodify.introduction";
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
