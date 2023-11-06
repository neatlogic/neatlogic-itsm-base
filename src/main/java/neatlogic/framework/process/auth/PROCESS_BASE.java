/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;
import neatlogic.framework.knowledge.auth.label.KNOWLEDGE;

import java.util.Arrays;
import java.util.List;

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

	@Override
	public List<Class<? extends AuthBase>> getIncludeAuths() {
		return Arrays.asList(KNOWLEDGE.class);
	}

}
