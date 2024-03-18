/*Copyright (C) 2024  深圳极向量科技有限公司 All Rights Reserved.

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

public class BATCH_REPORT_PROCESS_TASK extends AuthBase {

    @Override
    public String getAuthDisplayName() {
        return "批量上报权限";
    }

    @Override
    public String getAuthIntroduction() {
        return "对有权限服务可以批量上报工单";
    }

    @Override
    public String getAuthGroup() {
        return "process";
    }

    @Override
    public Integer getSort() {
        return 14;
    }
}
