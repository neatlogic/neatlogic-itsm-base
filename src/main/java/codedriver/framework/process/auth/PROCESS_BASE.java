/*
 * Copyright (c)  2021 TechSure Co.,Ltd.  All Rights Reserved.
 * 本内容仅限于深圳市赞悦科技有限公司内部传阅，禁止外泄以及用于其他的商业项目。
 */

package codedriver.framework.process.auth;

import codedriver.framework.auth.core.AuthBase;

public class PROCESS_BASE extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "IT服务基础权限";
	}

	@Override
	public String getAuthIntroduction() {
		return "查看IT服务相关基础功能";
	}

	@Override
	public String getAuthGroup() {
		return "process";
	}

	@Override
	public Integer getSort() {
		return 1;
	}

	@Override
	public boolean getIsDefault(){
		return true;
	}

}
