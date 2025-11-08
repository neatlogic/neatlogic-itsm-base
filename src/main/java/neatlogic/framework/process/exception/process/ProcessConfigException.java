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

package neatlogic.framework.process.exception.process;

import neatlogic.framework.exception.core.ApiRuntimeException;
import neatlogic.framework.util.$;

public class ProcessConfigException extends ApiRuntimeException {

    public enum Type {
        SLA, CONDITION, COPY, PRE_STEP_ASSIGN, PRE_STEP_ASSIGN_CONDITION_STEP
    }

    public ProcessConfigException(Type type, String name) {
        super(getMessage(type, name));
    }

    private static String getMessage(Type type, String name) {
        if (type == Type.SLA) {
            return $.t("nfpep.processconfigexception.sla", name);
        } else if (type == Type.CONDITION) {
            return $.t("nfpep.processconfigexception.condition", name);
        } else if (type == Type.COPY) {
            return $.t("nfpep.processconfigexception.copy", name);
        } else if (type == Type.PRE_STEP_ASSIGN) {
            return $.t("nfpep.processconfigexception.prestepassign", name);
        } else if (type == Type.PRE_STEP_ASSIGN_CONDITION_STEP) {
            return $.t("nfpep.processconfigexception.prestepassignconditionstep", name);
        }
        return "";
    }
}
