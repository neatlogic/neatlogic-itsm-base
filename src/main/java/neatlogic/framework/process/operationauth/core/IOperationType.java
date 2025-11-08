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

package neatlogic.framework.process.operationauth.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linbq
 * @since 2021/5/17 14:17
 **/
public interface IOperationType {
    String getValue();

    String getText();

    default List<String> getDefaultValue() {
        return new ArrayList<>();
    }

    default List<String> getGroupList() {
        return new ArrayList<>();
    }

    /**
     * 该操作权限级别，工单级别或步骤级别
     * @return
     */
    default OperationAuthHandlerType getOperationAuthHandlerType() {
        return null;
    }

    /**
     * 该操作权限是否可以授予给别人
     * @return
     */
    default boolean getCanProxyPermission() {
        return true;
    }
}
