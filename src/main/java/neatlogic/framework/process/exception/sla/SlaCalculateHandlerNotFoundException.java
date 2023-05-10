/*
Copyright(c) 2023 NeatLogic Co., Ltd. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package neatlogic.framework.process.exception.sla;

import neatlogic.framework.process.exception.core.ProcessTaskRuntimeException;

/**
 * @author linbq
 * @since 2021/11/22 14:28
 **/
public class SlaCalculateHandlerNotFoundException extends ProcessTaskRuntimeException {

    private static final long serialVersionUID = -6956721990886391194L;

    public SlaCalculateHandlerNotFoundException(String handler) {
        super("exception.process.slacalculatehandlernotfoundexception", handler);
    }
}
