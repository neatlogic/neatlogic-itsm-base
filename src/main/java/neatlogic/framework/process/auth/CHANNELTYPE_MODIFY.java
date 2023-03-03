package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class CHANNELTYPE_MODIFY extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "auth.process.channeltypemodify.name";
	}

	@Override
	public String getAuthIntroduction() {
		return "auth.process.channeltypemodify.introduction";
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
