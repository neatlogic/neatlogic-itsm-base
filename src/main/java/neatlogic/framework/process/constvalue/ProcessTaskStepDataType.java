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

import neatlogic.framework.util.I18n;

public enum ProcessTaskStepDataType implements IProcessTaskStepDataType {
    STEPDRAFTSAVE("stepdraftsave", new I18n("nfpc.processtaskstepdatatype.text.stepdraftsave")),
    AUTOMATIC("automatic", new I18n("nfpc.processtaskstepdatatype.text.automatic"));
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
