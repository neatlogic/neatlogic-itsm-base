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

package neatlogic.framework.process.constvalue;

import neatlogic.framework.util.I18n;

public enum ProcessTaskStepDataType implements IProcessTaskStepDataType {
    STEPDRAFTSAVE("stepdraftsave", new I18n("步骤草稿暂存")),
    AUTOMATIC("automatic", new I18n("auto节点数据"));
    private final String value;
    private final I18n text;

    ProcessTaskStepDataType(String value, I18n text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text.toString();
    }

}
