package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;

import java.util.Collections;
import java.util.List;

public class CATALOG_MODIFY extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "auth.process.catalogmodify.name";
	}

	@Override
	public String getAuthIntroduction() {
		return "auth.process.catalogmodify.introduction";
	}

	@Override
	public String getAuthGroup() {
		return "process";
	}

	@Override
	public Integer getSort() {
		return 4;
	}

	@Override
	public List<Class<? extends AuthBase>> getIncludeAuths(){
		return Collections.singletonList(PROCESS_BASE.class);
	}

}
