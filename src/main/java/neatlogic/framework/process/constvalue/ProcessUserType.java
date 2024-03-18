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

import neatlogic.framework.common.constvalue.IUserType;
import neatlogic.framework.dto.UserTypeVo;
import neatlogic.framework.util.I18n;

import java.util.HashMap;
import java.util.Map;

public enum ProcessUserType implements IUserType {
    MAJOR("major", new I18n("处理人"), true),
    MINOR("minor", new I18n("协助处理人"), true),
    //	AGENT("agent","代办人",true),
    OWNER("owner", new I18n("上报人"), true),
    REPORTER("reporter", new I18n("代报人"), true),
    WORKER("worker", new I18n("待处理人"), true),
    DEFAULT_WORKER("defaultworker", new I18n("异常处理人"), false),
    FOCUS_USER("focususer", new I18n("关注人"), false);

    private final String status;
    private final I18n text;
    private final boolean isShow;

    private ProcessUserType(String _status, I18n _text, boolean _isShow) {
        this.status = _status;
        this.text = _text;
        this.isShow = _isShow;
    }

    public String getValue() {
        return status;
    }

    public String getText() {
        return text.toString();
    }

    public boolean getIsShow() {
        return isShow;
    }

    public static String getValue(String _status) {
        for (ProcessUserType s : ProcessUserType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getText(String _status) {
        for (ProcessUserType s : ProcessUserType.values()) {
            if (s.getValue().equals(_status)) {
                return s.getText();
            }
        }
        return "";
    }


    @Override
    public UserTypeVo getUserType() {
        UserTypeVo vo = new UserTypeVo();
        vo.setModuleId(getModuleId());
        Map<String, String> map = new HashMap<>();
        for (ProcessUserType type : ProcessUserType.values()) {
            map.put(type.getValue(), type.getText());
        }
        vo.setValues(map);
        return vo;
    }

    @Override
    public String getModuleId() {
        return "process";
    }
}
