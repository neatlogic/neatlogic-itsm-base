/*
 *
 * Copyright (C) 2025  TechSure Co., Ltd.  All Rights Reserved.
 * This file is part of the NeatLogic software.
 * Licensed under the NeatLogic Sustainable Use License (NSUL), Version 4.x – 2025.
 * You may use this file only in compliance with the License.
 * See the LICENSE file distributed with this work for the full license text.
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 */

package neatlogic.framework.process.auth;

import neatlogic.framework.auth.core.AuthBase;
import neatlogic.framework.knowledge.auth.label.KNOWLEDGE;

import java.util.Arrays;
import java.util.List;

public class PROCESS_BASE extends AuthBase {

	@Override
	public String getAuthDisplayName() {
		return "nfpa.process_base.getauthdisplayname";
	}

	@Override
	public String getAuthIntroduction() {
		return "nfpa.process_base.getauthintroduction";
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
		return Arrays.asList(PROCESS.class, KNOWLEDGE.class);
	}

}
