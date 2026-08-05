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

package neatlogic.framework.process.workcenter.table;

import neatlogic.framework.util.I18n;
import org.springframework.stereotype.Component;

@Component
public class ProcessTaskStepCostSqlTable implements ISqlTable {

    @Override
    public String getName() {
        return "processtask_step_cost";
    }

    @Override
    public String getShortName() {
        return "psa";
    }

    public enum FieldEnum {
        PROCESSTASK_ID("processtask_id", new I18n("nfpwt.processtaskstepcostsqltable.text.processtask_id")),
        PROCESSTASK_STEP_ID("processtask_step_id", new I18n("nfpwt.processtaskstepcostsqltable.text.processtask_step_id")),
        START_USER_UUID("start_user_uuid", new I18n("nfpwt.processtaskstepcostsqltable.text.start_user_uuid")),
        START_OPERATE("start_operate", new I18n("nfpwt.processtaskstepcostsqltable.text.start_operate"));

        private final String name;
        private final I18n text;

        FieldEnum(String _value, I18n _text) {
            this.name = _value;
            this.text = _text;
        }

        public String getValue() {
            return name;
        }

        public String getText() {
            return text.toString();
        }
    }
}
