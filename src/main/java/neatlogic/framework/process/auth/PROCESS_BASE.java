/*Copyright (C) $today.year  深圳极向量科技有限公司 All Rights Reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

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
