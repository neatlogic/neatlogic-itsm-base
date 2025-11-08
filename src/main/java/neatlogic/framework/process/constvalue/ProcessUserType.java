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
