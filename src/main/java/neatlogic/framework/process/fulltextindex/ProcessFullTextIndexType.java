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

package neatlogic.framework.process.fulltextindex;

import neatlogic.framework.fulltextindex.core.IFullTextIndexType;
import neatlogic.framework.util.$;

/**
 * @author lvzk
 * @since 2021/03/23
 */
public enum ProcessFullTextIndexType implements IFullTextIndexType {
    PROCESSTASK("processtask", "term.itsm.processtask");
//    PROCESSTASK_FORM("processtask_form", "工单_表单");

    private final String type;
    private final String typeName;

    ProcessFullTextIndexType(String _type, String _typeName) {
        type = _type;
        typeName = _typeName;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getTypeName() {
        return $.t(typeName);
    }

    @Override
    public String getTypeName(String type) {
        for (ProcessFullTextIndexType t : values()) {
            if (t.getType().equals(type)) {
                return t.getTypeName();
            }
        }
        return "";
    }

    @Override
    public boolean isActiveGlobalSearch() {
        return true;
    }

}
